package demo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.repository.ContactRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringDataRestApplication.class)
@WebIntegrationTest
public class ContactsRestAPITest {
	private static final String API_BASE_URL = "http://localhost:8888/contacts";
	private static final String LINKS = "_links";
	private static final String SELF = "self";
	private static final String HREF = "href";

	@Autowired
	private ContactRepository contactRepository;

	//Required to Generate JSON content from Java objects
	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	//Test RestTemplate to invoke contacts APIs.
	private RestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void postGetAndDeleteContact() throws JsonProcessingException {
		// making sure we have nothing in db
		contactRepository.deleteAll();
		// building the Request body data
		Map<String, Object> requestBody = new HashMap();
		requestBody.put("firstName", "Oleg");
		requestBody.put("lastName", "Ushakov");
		requestBody.put("email", "oushakov@gmail.com");
		requestBody.put("phone", "9092899418");
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);

		// creating http entity object with request body and headers
		HttpEntity<String> httpEntity =
				new HttpEntity(OBJECT_MAPPER.writeValueAsString(requestBody), requestHeaders);

		//~~ Posting new contact
		Map<String, Object> apiResponse =
				restTemplate.postForObject(API_BASE_URL, httpEntity, Map.class, Collections.EMPTY_MAP);
		// validating the response of the API.
		assertNotNull(apiResponse);
		assertEquals(requestBody.get("firstname"), apiResponse.get("firstname"));
		assertEquals(requestBody.get("lastname"), apiResponse.get("lastname"));
		assertEquals(requestBody.get("email"), apiResponse.get("email"));
		assertEquals(requestBody.get("phone"), apiResponse.get("phone"));
		// obtaining the link to just created resource
		assertNotNull(apiResponse.get(LINKS));
		Map<String, Object> link = (Map<String, Object>)apiResponse.get(LINKS);
		assertNotNull(link.get(SELF));
		Map<String, Object> self = (Map<String, Object>)link.get(SELF);
		assertNotNull(self);
		String selfURL = (String)self.get(HREF);

		//~~ Getting just created resource using self link
		apiResponse = restTemplate.getForObject(selfURL, Map.class, Collections.EMPTY_MAP);
		// validating response
		assertNotNull(apiResponse);
		assertEquals(requestBody.get("firstname"), apiResponse.get("firstname"));
		assertEquals(requestBody.get("lastname"), apiResponse.get("lastname"));
		assertEquals(requestBody.get("email"), apiResponse.get("email"));
		assertEquals(requestBody.get("phone"), apiResponse.get("phone"));
		assertNotNull(apiResponse.get(LINKS));
		link = (Map<String, Object>)apiResponse.get(LINKS);
		assertNotNull(link.get(SELF));
		self = (Map<String, Object>)link.get(SELF);
		assertNotNull(self);
		assertEquals(selfURL, self.get(HREF));

		//~~ Deleting just created resource
		restTemplate.delete(selfURL);
		assertEquals(contactRepository.count(), 0);

	}
}
