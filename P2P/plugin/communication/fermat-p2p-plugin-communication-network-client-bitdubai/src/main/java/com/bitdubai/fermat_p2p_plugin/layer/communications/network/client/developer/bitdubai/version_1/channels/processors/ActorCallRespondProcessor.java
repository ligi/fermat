package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.processors;

import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.events.ActorFoundEvent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.events.NetworkClientActorCallConnectedEvent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ActorsProfileListMsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ResultDiscoveryTraceActor;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.P2pEventType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.endpoints.CommunicationsNetworkClientChannel;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.context.ClientContext;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.context.ClientContextItem;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.network_calls.NetworkClientCommunicationActorCall;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.NetworkClientCommunicationConnection;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.NetworkClientConnectionsManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.processors.ActorTraceDiscoveryQueryRespondProcessor</code>
 * process all packages received the type <code>PackageType.ACTOR_CALL_RESPOND</code><p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 19/05/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ActorCallRespondProcessor extends PackageProcessor {

    /*
     * Represent the networkClientConnectionsManager
     */
    private NetworkClientConnectionsManager networkClientConnectionsManager;

    /**
     * Constructor whit parameter
     *
     * @param communicationsNetworkClientChannel register
     */
    public ActorCallRespondProcessor(final CommunicationsNetworkClientChannel communicationsNetworkClientChannel) {
        super(
                communicationsNetworkClientChannel,
                PackageType.ACTOR_CALL_RESPOND
        );
        this.networkClientConnectionsManager =  (NetworkClientConnectionsManager) ClientContext.get(ClientContextItem.CLIENTS_CONNECTIONS_MANAGER);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived) {

        System.out.println("Processing new package received, packageType: "+packageReceived.getPackageType());
        ActorsProfileListMsgRespond actorsProfileListMsgRespond = ActorsProfileListMsgRespond.parseContent(packageReceived.getContent());

        if(actorsProfileListMsgRespond.getStatus() == ActorsProfileListMsgRespond.STATUS.SUCCESS){
            //raise event

            if(actorsProfileListMsgRespond.getProfileList() != null && actorsProfileListMsgRespond.getProfileList().size() > 0)
                goingThroughResultDiscoveryTraceActorList(actorsProfileListMsgRespond.getProfileList(), actorsProfileListMsgRespond.getNetworkServiceTypeIntermediate());

        }else{
            //there is some wrong
        }

    }

    /*
     * we go through ResultDiscoveryTraceActor list when get the Actor
     * then raise event notification
     */
    private synchronized void goingThroughResultDiscoveryTraceActorList(List<ResultDiscoveryTraceActor> resultList, NetworkServiceType networkServiceTypeIntermediate){

        Boolean isOnline = Boolean.FALSE;

        for(ResultDiscoveryTraceActor result : resultList){

            try {

                URL url = new URL("http://" + result.getNodeProfile().getIp() + ":" + result.getNodeProfile().getDefaultPort() +
                        "/fermat/rest/api/v1/online/component/actor/" + result.getActorProfile().getIdentityPublicKey());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String respond = reader.readLine();

                if (conn.getResponseCode() == 200 && respond != null && respond.contains("success")) {
                    /*
                    * Decode into a json Object
                    */
                    JsonParser parser = new JsonParser();
                    JsonObject respondJsonObject = (JsonObject) parser.parse(respond.trim());

                    isOnline = respondJsonObject.get("isOnline").getAsBoolean();

                    if(isOnline){

                        System.out.print("Actor Found in the NODE: " + result.getNodeProfile().toJson());

                        String uriToNode =  result.getNodeProfile().getIp() + ":" + result.getNodeProfile().getDefaultPort();

                        /*
                         * if the connection to the node already exists use the current NetworkClientCommunicationConnection
                         * else request a new NetworkClientCommunicationConnection to that Node
                         */
                        if(networkClientConnectionsManager.getActiveConnectionsToExternalNodes().containsKey(uriToNode)) {

                            NetworkClientCommunicationConnection connection = networkClientConnectionsManager.getActiveConnectionsToExternalNodes().get(uriToNode);

                            NetworkClientCommunicationActorCall actorCall = new NetworkClientCommunicationActorCall(
                                    networkServiceTypeIntermediate,
                                    result.getActorProfile(),
                                    connection
                            );

                            connection.addCall(actorCall);

                            /*
                             * Create a raise a new event whit the NETWORK_CLIENT_CONNECTION_SUCCESS
                             */
                            FermatEvent actorCallConnected = getEventManager().getNewEvent(P2pEventType.NETWORK_CLIENT_ACTOR_CALL_CONNECTED);
                            actorCallConnected.setSource(EventSource.NETWORK_CLIENT);

                            ((NetworkClientActorCallConnectedEvent) actorCallConnected).setNetworkClientCall(actorCall);

                            /*
                             * Raise the event
                             */
                            System.out.println("ActorCallRespondProcessor - Raised a event = P2pEventType.NETWORK_CLIENT_ACTOR_CALL_CONNECTED");
                            getEventManager().raiseEvent(actorCallConnected);

                        } else {

                            networkClientConnectionsManager.requestConnectionToExternalNode(uriToNode);

                            // TODO see how to do to check this type of connection maybe a different check in

                        }

                        break;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                // todo we should handle the exceptions here.
            }

        }

        if(!isOnline) {
            System.out.print("Actor NOT Found in the list of NODES");
            //raise event not success found actor
        }

    }

}
