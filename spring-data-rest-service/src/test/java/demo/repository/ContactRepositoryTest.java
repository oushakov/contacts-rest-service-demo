package demo.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import demo.SpringDataRestApplication;
import demo.domain.Contact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringDataRestApplication.class)
public class ContactRepositoryTest {
	@Autowired
	private ContactRepository contactRepository;

	@Test
	public void testSaveAndFetchContact() {
		// making sure we have nothing in db
		contactRepository.deleteAll();
		Contact contact = new Contact();
		contact.setFirstName("Oleg");
		contact.setLastName("Ushakov");
		contact.setEmail("oushakov@gmail.com");
		contact.setPhone("9092899418");
		contactRepository.save(contact);
		assertNotNull(contact.getId()); //not null after save

		Contact fetchedContact = contactRepository.findOne(contact.getId());
		//should not be null
		assertNotNull(fetchedContact);

		//should be equal
		assertEquals(contact.getId(), fetchedContact.getId());
		assertEquals(contact.getFirstName(), fetchedContact.getFirstName());
		assertEquals(contact.getLastName(), fetchedContact.getLastName());
		assertEquals(contact.getEmail(), fetchedContact.getEmail());
		assertEquals(contact.getPhone(), fetchedContact.getPhone());

		//update phone and save
		fetchedContact.setPhone("9092899419");
		contactRepository.save(fetchedContact);

		//get from DB, should be updated
		Contact fetchedUpdatedContact = contactRepository.findOne(fetchedContact.getId());
		assertEquals(fetchedContact.getPhone(), fetchedUpdatedContact.getPhone());

		//verify count of contacts in DB
		long contactCount = contactRepository.count();
		assertEquals(contactCount, 1);
	}
}
