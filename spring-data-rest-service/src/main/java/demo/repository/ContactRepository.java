package demo.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import demo.domain.Contact;

@Repository
public interface ContactRepository extends PagingAndSortingRepository<Contact, Long> {
	List<Contact> findByLastName(@Param("name") String name);
}
