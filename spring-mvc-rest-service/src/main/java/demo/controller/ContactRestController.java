package demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import demo.domain.Contact;
import demo.service.ContactService;

@RestController
@RequestMapping(value = "/contacts")
public class ContactRestController {
	@Autowired
	private ContactService contactService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Contact> getAllContacts() {
		return contactService.findAllContacts();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	Contact addContact(@RequestBody Contact input) {
		return contactService.save(input);
	}

	@RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Contact getContact(@PathVariable("id") long id) {
		return contactService.findContactById(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String deleteContact(@PathVariable("id") long id) {
		contactService.deleteContact(id);

		return "redirect:/contacts";
	}
}
