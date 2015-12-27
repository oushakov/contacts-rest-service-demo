package demo.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import demo.domain.Contact;

@Repository
public interface ContactRepository extends PagingAndSortingRepository<Contact, Long> {
	List<Contact> findByLastName(String name);
}
