package whatsapp;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static spark.Spark.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class SmsApp {
	public static Integer mobilenumber = null;
	public static Integer mpesanumber = null;
	public static Integer firstNameLength = null;
	public static Integer amount = null;
	public static Integer secondNameLength = null;
	public static Integer nationalID = null;
	public static String message = null; //Message Initialization
	public static String description = null; //Message Initialization
	public static String messageFromMpesa = null; //Message Initialization
	public static String releaseFromSuspenseAccount = null; //Message Initialization
    public static void main(String[] args) throws UnsupportedEncodingException {
    	Mpesa m=new Mpesa(Constants.APP_KEY,Constants.APP_SECRET);
    	String shortcode = "174379";
        String key = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
        SimpleDateFormat ssdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
        String paykey = shortcode + key + ssdf.format(timestamp1.getTime());
        byte[] bytes = paykey.getBytes("ISO-8859-1");
        String encoded = Base64.getEncoder().encodeToString(bytes);
        get("/", (req, res) -> "Hello Web");
        post("/sms", (req, res) -> {
            res.type("application/xml");
            //This body string should appear at the top immediately after post function
            String body = req.queryParams("Body");
            System.out.println(body);
            
            if (body.equals("mpesa")||body.equals("MPESA")||body.equals("Mpesa")) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody bodyy = RequestBody.create(mediaType, "{\"Initiator\": \","
            		+ "\"SecurityCredential\":\","
            		+ "\"CommandID\":\","
            		+ "\"SenderIdentifierType\":\","
            		+ "\"RecieverIdentifierType\":\","
            		+ "\"Amount\":\","
            		+ "\"PartyA\":\","
            		+ "\"PartyB\":\","
            		+ "\"AccountReference\":\","
            		+ "\"Remarks\":\","
            		+ "\"QueueTimeOutURL\":\","
            		+ "\"Remarks\":\","
            		+ "\"QueueTimeOutURL\":\"http://your_timeout_url\","
            		+ "\"ResultURL\":\"http://your_result_url\","
            		+ "}");
            Request request = new Request.Builder()
            	    .url("https://sandbox.safaricom.co.ke/mpesa/c2b/v1/simulate")
            	    .post(bodyy)
            	    .addHeader("authorization", "Bearer ACCESS_TOKEN")
            	    .addHeader("content-type", "application/json")
            	    .build();
            
            Response response = client.newCall(request).execute();
            //System.out.println(response);
            //End of Mpesa C2B Response Function`
            }
            else if(body.equals("start")||body.equals("START")||body.equals("Start")) {
              // Sent Initial Message to Clients
              message = "*Welcome to _Pesa Suspend!_*"
              		+ "\n"
              		+ "\n *_Pesa Suspend_* helps your service providers *TRUST YOU* by showing them your pocket is loaded."
              		+ "\n"
              		+ "\n*Pesa Suspend* allows you suspend money as milestones to your service providers account. "
              		+ "\nService Providers could be your *_Plumber_*, *_Courrier_* , *_Masonry_* , "
              		+ "*_Electronics Technician_* etc."
              		+ "\n"
              		+ "\nThis way your *Service Providers* deliver services for the milestones created to their *_Pesa Suspend_* "
              		+ "account without fear "
              		+ "of receiving the payment."
              		+ "\nThe same way your service providers can see money has been deposited/suspended into "
              		+ "their *_Pesa Suspend_* account but cannot withdraw it until they have *completed* the tasks."
              		+ "\n"
              		+ "\n*When you create milestone(s) for a particular service, you need to describe it properly.* "
              		+ "\nOnce each milestone service is completed to your satisfaction, you can release "
              		+ "the milestone from your *Pesa Suspend* Account or *_vise versa_*, if your service provider has completed a "
              		+ "task, they can request release of the milestone payments."
              		+ "\n"
              		+ "\n_Type_ *SETUP* _to create your *Pesa Suspend* account_";
            } else if (body.equals("setup")||body.equals("SETUP")||body.equals("Setup")) {
              // Capture Mobile Number
              message = "To proceed please reply with your phone number in the format 2547******** "
              			+ "to setup your Pesa Suspend Account";
              mobilenumber = message.length();
              System.out.println(mobilenumber);
              //System.out.println(mobilenumber.toString());
            }else if (mobilenumber!=null){
                // Capture Mobile Number
            	mobilenumber = null;
                message = "Enter Your First Name";
                firstNameLength = message.length();
                System.out.println(firstNameLength);
            }else if (firstNameLength!=null) {
                  // Capture Mobile Number
                 firstNameLength = null;
                  message = "Enter Your Second Name";
                  secondNameLength = message.length();
                  System.out.println(secondNameLength);
            }
            else if (secondNameLength!=null) {
                // Capture National ID	`` Number
            	secondNameLength = null;
                message = "Enter Your National ID number";
                nationalID = message.length();
                System.out.println(nationalID);
          } 
            else if (nationalID!=null) {
                // Capture National ID	`` Number
            	nationalID = null;
                message = "Thank you for setting up your *Pesa Suspend* Account"
                		+ "\nDescribe Service you want to *_Pay For_* or *_Create Milestones For_*"
                		+ "\ne.g. Painting 10*10 meter square wall"
                		+ "\n";
                description = message;
                System.out.println(description);
          } 
            else if (description!=null) {
                // Capture National ID	`` Number
            	description = null;
                message = "Enter MPESA mobile number of the person you wish to pay"
                		+ "\n in the format _* 07******** *_";
                mpesanumber = message.length();
                System.out.println(mpesanumber);
          } 
            else if (mpesanumber!=null) {
                // Capture National ID	`` Number
            	mpesanumber = null;
                message = "Wait for MPESA to enter your PIN"
                		+ "\n"
                		+ 
                        m.STKPushSimulation("174379",encoded,ssdf.format(timestamp1.getTime()),"CustomerPayBillOnline","10","254708374149","254708374149","174379","http://beannsofts.com/c2bvalidation/","test1","test1"
                        		+ "\n");       
                ;
                messageFromMpesa = message;
                System.out.println(messageFromMpesa);
                System.out.println(mpesanumber);
            }
            else if (messageFromMpesa!=null) {
                // Capture National ID	`` Number
            	messageFromMpesa = null;
                message = "*You have suspended KSHS:500 on Ken Musya's *Pesa Suspend Account*"
                		+ "to Paint a 50*50 square meter wall.*"
                		+ "\n"
                		+ "\nOnce Ken Musya finishes on this task within deadline, you can type"
                		+ "ken Musya's mpesa number here to initiate the process of releasing "
                		+ "milestone payment from his suspense account."
                		+ "\n"
                		+ "\n*_Thank You for Using Our Services_*";
                releaseFromSuspenseAccount = message;
                System.out.println(releaseFromSuspenseAccount);
          } else if(!body.equals("")){
        	// Sent Initial Message to Clients
              message = "*Welcome to _Pesa Suspend!_*"
              		+ "\n"
              		+ "\n *_Pesa Suspend_* helps your service providers *TRUST YOU* by showing them your pocket is loaded."
              		+ "\n"
              		+ "\n*Pesa Suspend* allows you suspend money as milestones to your service providers account. "
              		+ "\nService Providers could be your *_Plumber_*, *_Courrier_* , *_Masonry_* , "
              		+ "*_Electronics Technician_* etc."
              		+ "\n"
              		+ "\nThis way your *Service Providers* deliver services for the milestones created to their *_Pesa Suspend_* "
              		+ "account without fear "
              		+ "of receiving the payment."
              		+ "\nThe same way your service providers can see money has been deposited/suspended into "
              		+ "their *_Pesa Suspend_* account but cannot withdraw it until they have *completed* the tasks."
              		+ "\n"
              		+ "\n*When you create milestone(s) for a particular service, you need to describe it properly.* "
              		+ "\nOnce each milestone service is completed to your satisfaction, you can release "
              		+ "the milestone from your *Pesa Suspend* Account or *_vise versa_*, if your service provider has completed a "
              		+ "task, they can request release of the milestone payments."
              		+ "\n"
              		+ "\n_Type_ *SETUP* _to create your *Pesa Suspend* account_";
          }
            // Create a TwiML response and add our friendly message.
            Body messageBody = new Body.Builder(message).build();
            Message sms = new Message.Builder().body(messageBody).build();
            MessagingResponse twiml = new MessagingResponse.Builder().message(sms).build();
            try {
              System.out.println(twiml.toXml());
            } catch (TwiMLException e) {
              e.printStackTrace();
            }
            return twiml.toXml();
        });
    }
}
