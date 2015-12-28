package demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.domain.Contact;
import demo.repository.ContactRepository;

@Service
@Transactional
public class ContactService
{
	@Autowired
	private ContactRepository contactRepository;
	
	public Contact save(Contact contact)
	{
		if(checkContactExists(contact.getEmail())){
			throw new RuntimeException("Email ["+contact.getEmail()+"] already exist");
		}
		return contactRepository.save(contact);
	}

	public Contact findContactById(Long id)
	{
		return contactRepository.findOne(id);
	}

	public List<Contact> findAllContacts()
	{
		return contactRepository.findAll();
	}

	public boolean checkContactExists(String email)
	{
		return contactRepository.findByEmail(email).size() > 0;
	}

	public boolean checkContactExists(Contact contact)
	{
		return contactRepository.findByEmail(contact.getEmail()).size() > 0;
	}

	public void deleteContact(Contact contact) {
		contactRepository.delete(contact);
	}
	public void deleteContact(long id) {
		contactRepository.delete(id);
	}
}
