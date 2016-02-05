package com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.adapters.ChatListAdapter;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.sessions.ChatSession;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.settings.ChatSettings;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_cht_android_sub_app_chat_bitdubai.R;
import com.bitdubai.fermat_cht_api.all_definition.enums.ChatStatus;
import com.bitdubai.fermat_cht_api.all_definition.enums.MessageStatus;
import com.bitdubai.fermat_cht_api.all_definition.enums.TypeMessage;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantGetChatException;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantGetContactException;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantGetMessageException;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantSaveChatException;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantSaveContactException;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantSaveMessageException;
import com.bitdubai.fermat_cht_api.layer.middleware.interfaces.Chat;
import com.bitdubai.fermat_cht_api.layer.middleware.interfaces.Contact;
import com.bitdubai.fermat_cht_api.layer.middleware.interfaces.Message;
import com.bitdubai.fermat_cht_api.layer.middleware.utils.ChatImpl;
import com.bitdubai.fermat_cht_api.layer.middleware.utils.ContactImpl;
import com.bitdubai.fermat_cht_api.layer.middleware.utils.MessageImpl;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatManager;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatModuleManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.holders.ChatsListHolder;


/**
 * Chat List Fragment
 *
 * @author Jose Cardozo josejcb (josejcb89@gmail.com) on 19/01/16
 * @version 1.0
 * Upd
 *
 */

public class ChatListFragment extends AbstractFermatFragment{

    // Defines a tag for identifying log entries
    //private static final String TAG = "ChatListFragment";

    // Bundle key for saving previously selected search result item
    //private static final String STATE_PREVIOUSLY_SELECTED_KEY =    "SELECTED_ITEM";
//    private ChatListAdapter adapter; // The main query adapter
//    private ImageLoader mImageLoader; // Handles loading the chat image in a background thread
//    private String mSearchTerm; // Stores the current search query term
//
//    private OnChatInteractionListener mOnChatSelectedListener;
//
//    // Stores the previously selected search item so that on a configuration change the same item
//    // can be reselected again
//    private int mPreviouslySelectedSearchItem = 0;
//    //public ArrayList<ContactList> contactList;
//   // private ListView contactsContainer;
//    //private ContactsAdapter adapter;
//
//    // Whether or not the search query has changed since the last time the loader was refreshed
//    private boolean mSearchQueryChanged;
//
//    // Whether or not this fragment is showing in a two-pane layout
      private boolean mIsTwoPaneLayout;
//
//    // Whether or not this is a search result view of this fragment, only used on pre-honeycomb
//    // OS versions as search results are shown in-line via Action Bar search from honeycomb onward
//    private boolean mIsSearchResultView = false;

    /*boolean dualPane;
    private static int currentCheckPosition = 0;
    private ProgressBar progressBar;
    private static ChatsList chats;
    private LinearLayout layout;
    private ProgressDialog mProgressDialog;
    private boolean fragmentStopped = false;

    private FermatTextView noChatsMessage;

    private long clickedId;*/


    private ChatManager chatManager;
    private ChatModuleManager moduleManager;
    private ErrorManager errorManager;
    private SettingsManager<ChatSettings> settingsManager;
    private ChatSession chatSession;

    ListView list;

    SwipeRefreshLayout mSwipeRefreshLayout;
    Map<UUID,Chat> chatcreated=new HashMap<UUID,Chat>();
    Map<UUID,List<Message>> messagecreated=new HashMap<UUID,List<Message>>();
    Map<String,Contact>contactcreated=new HashMap<String,Contact>();
    ArrayList<String> infochat=new ArrayList<String>();


    //   static HashMap<Integer,List<String>> chatinfo=new HashMap<Integer,List<String>>();
   // static Integer[] imgid=new Integer[6];
    ArrayList<ArrayList<String>> chatinfo=new ArrayList<ArrayList<String>>();   //work
    ArrayList imgid=new ArrayList();
   /* Integer[] imgid={R.drawable.ken,
    R.drawable.sas,
    R.drawable.koj,
    R.drawable.veg,
    R.drawable.ren,
    R.drawable.pat
    };
*/


    public static ChatListFragment newInstance() {
        return new ChatListFragment();}

    //Create a object list with
    //contactcreated(Map<remoteactorPK,Map<PlatformComponentType,Contact>)
    //chatcreated(chatid,chat);
    //Message(Map<chatid,Kist<Message>)

    public void listChatcreated(){
        String tempcon;
        UUID tempchat;
        UUID tempmessage;
        try {
            chatcreated.clear();
            messagecreated.clear();
            contactcreated.clear();

            if((chatManager.getChats().isEmpty())){
                System.out.println("/n/nSORRY NO CHAT FOR YOU/n/n");
            }else{
                for (int i=0;i<chatManager.getChats().size();i++){
                    tempchat=chatManager.getChats().get(i).getChatId();
                    if(!(chatcreated.containsKey(tempchat))) {
                        chatcreated.put(tempchat, chatManager.getChatByChatId(chatManager.getChats().get(i).getChatId()));
                    }
                }
                for (int i=0;i<chatManager.getMessages().size();i++){
                        tempmessage=chatManager.getMessages().get(i).getMessageId();
                        if(!messagecreated.containsKey((tempmessage))){
                            messagecreated.put(tempmessage,chatManager.getMessageByChatId(tempmessage));
                        }

                }
                for (int i=0;i<chatManager.getContacts().size();i++){
                    tempcon=chatManager.getContacts().get(i).getRemoteActorPublicKey();
                    if(!(contactcreated.containsKey(tempcon))) {
                        contactcreated.put(tempcon,chatManager.getContacts().get(i));
                    }
                }
            }

        } catch (CantGetChatException e) {
            e.printStackTrace();
        } catch (CantGetMessageException e) {
            e.printStackTrace();
        } catch (CantGetContactException e) {
            e.printStackTrace();
        }
    }


    //contactcreated(Map<remoteactorPK,Map<PlatformComponentType,Contact>)
    //chatcreated(chatid,chat);
    //Message(Map<chatid,Kist<Message>)


    //Load chatlist viewdata

    public void chatlistview (){

        int sizeofmessagelist=0;
        UUID chatidtemp;
        String contactid;
        Contact tempcontac=new ContactImpl();
        String name,message,datemessage,chatid;
        try {
              infochat.clear();
        for (int i=0;i<chatcreated.size();i++){

            chatidtemp=chatManager.getChats().get(i).getChatId();
            tempcontac=contactcreated.get(chatManager.getChats().get(i).getRemoteActorPublicKey());
            name=tempcontac.getRemoteName();
            sizeofmessagelist=chatManager.getMessageByChatId(chatidtemp).size();
            message=chatManager.getMessageByChatId(chatidtemp).get(sizeofmessagelist - 1).getMessage();
            datemessage=chatManager.getChatByChatId(chatidtemp).getLastMessageDate().toString();
            chatid=chatidtemp.toString();
            contactid=String.valueOf(chatManager.getMessageByChatId(chatidtemp).get(0).getContactId());
            infochat.add(name+"@#@#"+message+"@#@#"+datemessage+"@#@#"+chatid+"@#@#"+contactid+"@#@#");
            imgid.add(R.drawable.ken);
        }

        } catch (CantGetChatException e) {
            e.printStackTrace();
        } catch (CantGetMessageException e) {
            e.printStackTrace();
        }
    }

    @Override
     public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            chatSession = ((ChatSession) appSession);
            moduleManager = chatSession.getModuleManager();
            chatManager = moduleManager.getChatManager();
            //settingsManager = moduleManager.getSettingsManager();
            errorManager = appSession.getErrorManager();
      //      filldatabase();
        } catch (Exception e) {
            if (errorManager != null)
                errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }

        // Check if this fragment is part of a two-pane set up or a single pane by reading a
        // boolean from the application resource directories. This lets allows us to easily specify
        // which screen sizes should use a two-pane layout by setting this boolean in the
        // corresponding resource size-qualified directory.
    //    mIsTwoPaneLayout = getResources().getBoolean(R.bool.has_two_panes);

        // Let this fragment contribute menu items
        setHasOptionsMenu(true);
   }


   void updatevalues(){
       listChatcreated();
       chatlistview();

   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//System.out.println("**********LISTA:"+chatinfo.get(0).get(0)+" - "+chatinfo.get(0).get(1)+" - "+chatinfo.get(0).get(2));
     //   setContentView(getActivity());
        View layout = inflater.inflate(R.layout.chats_list_fragment, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_container);
        updatevalues();
        final ChatListAdapter adapter=new ChatListAdapter(getActivity(), infochat, imgid);
        list=(ListView)layout.findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = infochat.get(position).toString();
                String values = infochat.get(position);
                List<String> converter = new ArrayList<String>();
                converter.addAll(Arrays.asList(values.split("@#@#")));
                Toast.makeText(getActivity(), Slecteditem, Toast.LENGTH_SHORT).show();
                appSession.setData("whocallme", "chatlist");
                appSession.setData("contactid", UUID.fromString(converter.get(4)));
                changeActivity(Activities.CHT_CHAT_OPEN_MESSAGE_LIST, appSession.getAppPublicKey());
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("LEAN ON");
                 //       Toast.makeText(getActivity(), "Wake up Kakaroto", Toast.LENGTH_SHORT).show();

                        try {
                            System.out.println("Threar UI corriendo");
                            //TODO: fix this
                            if (!chatManager.getContacts().isEmpty()) {
                                especialfilldatabase();
                                updatevalues();
                                adapter.refreshEvents(infochat, imgid);
                            } else {
                                Toast.makeText(getActivity(), "No Contact now", Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            //TODO: fix this
                            e.printStackTrace();
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });
        return layout;
    }

    //FINALLY

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate the menu items
        inflater.inflate(R.menu.chat_list_menu, menu);
        // Locate the search item
        MenuItem searchItem = menu.findItem(R.id.menu_search);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_open_chat) {
            changeActivity(Activities.CHT_CHAT_OPEN_CONTACTLIST, appSession.getAppPublicKey());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    void especialfilldatabase(){
        ChatImpl dato;
        MessageImpl mess;
        ContactImpl cont=new ContactImpl();
        Calendar c = Calendar.getInstance();
        UUID chatid;
        UUID messageid;
        UUID contactid;
        String pkremote;


        try {

            messageid=UUID.randomUUID();
            pkremote=String.valueOf(Math.random() * 1000);

            Long startDate=System.currentTimeMillis();


            //Chat
            for (int i=0;i<chatManager.getContacts().size();i++) {


                if(!infochat.contains(chatManager.getContacts().get(i).getContactId())) {
                    chatid = UUID.randomUUID();
                    contactid = chatManager.getContacts().get(i).getContactId();


                    cont.setContactId(contactid);
                    cont.setRemoteName(chatManager.getContacts().get(i).getRemoteName());
                    cont.setAlias("other");
                    cont.setRemoteActorType(chatManager.getContacts().get(i).getRemoteActorType());
                    cont.setRemoteActorPublicKey(chatManager.getContacts().get(i).getRemoteActorPublicKey());
                    cont.setCreationDate(startDate);

                    chatManager.saveContact(cont);


                    dato = new ChatImpl(chatid,
                            UUID.randomUUID(),
                            PlatformComponentType.ACTOR_ASSET_ISSUER,
                            appSession.getAppPublicKey(),
                            chatManager.getContacts().get(i).getRemoteActorType(),
                            chatManager.getContacts().get(i).getRemoteActorPublicKey(),
                            "Nuevo",
                            ChatStatus.VISSIBLE,
                            new Timestamp(startDate),
                            new Timestamp(startDate));

                    chatManager.saveChat(dato);


                    mess = new MessageImpl();
                    mess.setType(TypeMessage.INCOMMING);
                    mess.setStatus(MessageStatus.DELIVERED);
                    mess.setChatId(chatid);
                    mess.setMessage("HOLA A TODOS");
                    mess.setMessageDate(new Timestamp(startDate));
                    mess.setMessageId(UUID.randomUUID());
                    mess.setContactId(contactid);

                    chatManager.saveMessage(mess);
                }
            }
        }
        catch (CantGetContactException e) {
            System.out.println("/n/n CHT FILLDATA SAVECONTACT:"+e);
            e.printStackTrace();
        }catch (CantSaveMessageException e) {
            System.out.println("/n/n CHT FILLDATA SAVEMESSAGE:"+e);
            e.printStackTrace();
        }catch (CantSaveContactException e) {
            System.out.println("/n/n CHT FILLDATA SAVECONTACT:"+e);
            e.printStackTrace();
        }catch (CantSaveChatException e) {
            System.out.println("/n/n CHT FILLDATA SAVECHAT:"+e);
            e.printStackTrace();
        }
    }









}
