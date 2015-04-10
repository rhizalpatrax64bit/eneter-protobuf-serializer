package net.client;

import message.declarations.MessageDeclarations.*;
import eneter.messaging.dataprocessing.serializing.ISerializer;
import eneter.messaging.diagnostic.EneterTrace;
import eneter.messaging.endpoints.typedmessages.*;
import eneter.messaging.messagingsystems.messagingsystembase.*;
import eneter.messaging.messagingsystems.tcpmessagingsystem.TcpMessagingSystemFactory;
import eneter.net.system.EventHandler;
import eneter.protobuf.ProtoBufSerializer;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class AndroidNetCommunicationClientActivity extends Activity
{
    // UI controls
    private Handler myRefresh = new Handler();
    private EditText myMessageTextEditText;
    private EditText myResponseEditText;
    private Button mySendRequestBtn;
    
    
    // Sender sending MyRequest and as a response receiving MyResponse.
    private IDuplexTypedMessageSender<MyResponse, MyRequest> mySender;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Get UI widgets.
        myMessageTextEditText = (EditText) findViewById(R.id.messageTextEditText);
        myResponseEditText = (EditText) findViewById(R.id.messageLengthEditText);
        mySendRequestBtn = (Button) findViewById(R.id.sendRequestBtn);
        
        // Subscribe to handle the button click.
        mySendRequestBtn.setOnClickListener(myOnSendRequestClickHandler);
        
        // Open the connection in another thread.
        // Note: From Android 3.1 (Honeycomb) or higher
        //       it is not possible to open TCP connection
        //       from the main thread.
        Thread anOpenConnectionThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        openConnection();
                    }
                    catch (Exception err)
                    {
                        EneterTrace.error("Open connection failed.", err);
                    }
                }
            });
        anOpenConnectionThread.start();
    }
    
    @Override
    public void onDestroy()
    {
        // Stop listening to response messages.
        mySender.detachDuplexOutputChannel();
        
        super.onDestroy();
    } 
    
    private void openConnection() throws Exception
    {
        // Instantiate Protocol Buffer based serializer.
        ISerializer aSerializer = new ProtoBufSerializer();
        
        // Create sender sending MyRequest and as a response receiving MyResponse
        // The sender will use Protocol Buffers to serialize/deserialize messages. 
        IDuplexTypedMessagesFactory aSenderFactory = new DuplexTypedMessagesFactory(aSerializer);
        mySender = aSenderFactory.createDuplexTypedMessageSender(MyResponse.class, MyRequest.class);
        
        // Subscribe to receive response messages.
        mySender.responseReceived().subscribe(myOnResponseHandler);
        
        // Create TCP messaging for the communication.
        // Note: 10.0.2.2 is a special alias to the loopback (127.0.0.1)
        //       on the development machine.
        IMessagingSystemFactory aMessaging = new TcpMessagingSystemFactory();
        
        IDuplexOutputChannel anOutputChannel
            = aMessaging.createDuplexOutputChannel("tcp://10.0.2.2:8060/");
            //= aMessaging.createDuplexOutputChannel("tcp://192.168.1.102:8060/");
        
        // Attach the output channel to the sender and be able to send
        // messages and receive responses.
        mySender.attachDuplexOutputChannel(anOutputChannel);
    }
    
    private void onSendRequest(View v)
    {
        // Create the request message using ProtoBuf builder pattern.
        final MyRequest aRequestMsg = MyRequest.newBuilder()
                .setText(myMessageTextEditText.getText().toString())
                .build();
        
        // Send the request message.
        try
        {
            mySender.sendRequestMessage(aRequestMsg);
        }
        catch (Exception err)
        {
            EneterTrace.error("Sending the message failed.", err);
        }
        
    }
    
    private void onResponseReceived(Object sender,
                                    final TypedResponseReceivedEventArgs<MyResponse> e)
    {
        // Display the result - returned number of characters.
        // Note: Marshal displaying to the correct UI thread.
        myRefresh.post(new Runnable()
            {
                @Override
                public void run()
                {
                    myResponseEditText.setText(Integer.toString(e.getResponseMessage().getLength()));
                }
            });
    }
    
    private EventHandler<TypedResponseReceivedEventArgs<MyResponse>> myOnResponseHandler
            = new EventHandler<TypedResponseReceivedEventArgs<MyResponse>>()
    {
        @Override
        public void onEvent(Object sender,
                            TypedResponseReceivedEventArgs<MyResponse> e)
        {
            onResponseReceived(sender, e);
        }
    };
    
    private OnClickListener myOnSendRequestClickHandler = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            onSendRequest(v);
        }
    };
}