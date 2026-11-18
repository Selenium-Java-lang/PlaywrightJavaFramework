package tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class APITest {
    Playwright playwright;
    APIRequestContext apiRequest;
    @Test
    public void e2eApiTest() {
        playwright = Playwright.create();
        HashMap<String,String> loginPayload = new HashMap<>();
        loginPayload.put("email", "chuha@gmail.com");
        loginPayload.put("password", "QAWSplm@1234");
         //browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
         apiRequest = playwright.request().newContext();
         APIResponse loginResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login"
         , RequestOptions.create().setData(loginPayload));
        Assert.assertTrue(loginResponse.ok());
        String jsonResponse = new String(loginResponse.body());
        System.out.println(jsonResponse);
        String token = JsonPath.read(loginResponse.text(),"$.token");
        System.out.println(token);
        //create event
        String eventTitle = "Playwright API Testing";
        HashMap<Object,Object> createEventPayload = new HashMap<>();
        createEventPayload.put("title", eventTitle);
        createEventPayload.put("description", "api");
        createEventPayload.put("category", "Conference");
        createEventPayload.put("venue", "Main Road");
        createEventPayload.put("city", "Bangalore");
        createEventPayload.put("eventDate", "2026-12-15T09:00:00.000Z");
        createEventPayload.put("price", 1500);
        createEventPayload.put("totalSeats", 500);

        APIResponse eventResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events"
        ,RequestOptions.create().setHeader("Authorization","Bearer "+token)
                        .setData(createEventPayload));
        Assert.assertTrue(eventResponse.ok(),"Create Event API should succeed");
        int eventId = JsonPath.read(eventResponse.text(), "$.data.id");
        System.out.println("Event created and its ID is "+eventId);
        //get event
        APIResponse retrieveEvents = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page", 1).setQueryParam("limit",12)
                        .setHeader("Authorization","Bearer "+token));
        Assert.assertTrue(retrieveEvents.ok(), "Event Retrieval API should succeed");
        System.out.println(retrieveEvents.text());
        List<Integer> allEventsIds = JsonPath.read(retrieveEvents.text(),"$.data[*].id");
        Assert.assertTrue(allEventsIds.contains(eventId), "Created event should appear in events list");
        //get event with id

        APIResponse getEventResponse = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventId,
                RequestOptions.create().setHeader("Authorization", "Bearer "+token));
        Assert.assertTrue(getEventResponse.ok());
        System.out.println(getEventResponse.status());
        System.out.println(getEventResponse.text());
        //delete event
        APIResponse deleteEventResponse = apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventId,
                RequestOptions.create().setHeader("Authorization","Bearer "+token));
        System.out.println(deleteEventResponse.status());
        System.out.print(deleteEventResponse.text());
        //verify the deletion is success --> getEvents and confirm that event id does not exist anymore
        APIResponse verifyRes = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setHeader("Authorization", "Bearer "+token)
                        .setQueryParam("page", 1)
                        .setQueryParam("limit", 12));
        Assert.assertTrue(verifyRes.ok(), "post event delete list call should succeed");
        List<String> titlesAfterDelete = JsonPath.read(verifyRes.text(),"$.data[*].title");
        Assert.assertFalse(titlesAfterDelete.contains(eventTitle),
                "Deleted event must not appear in events list");





    }


    }

