package de.cmuellerke.kundenverwaltung.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import de.cmuellerke.kundenverwaltung.models.UserEntity;
import de.cmuellerke.kundenverwaltung.payload.user.UserDTO;
import de.cmuellerke.kundenverwaltung.repository.UserRepository;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;

@Service
public class UserAdministrationService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	public List<UserDTO> getAllUsers() {
		List<UserEntity> allUsers = userRepository.findByTenantId(TenantContext.getTenantId());

		return allUsers.stream().map(user -> {
			UserDTO userDTO = new UserDTO();
			modelMapper.map(user, userDTO);
			return userDTO;
		}).collect(Collectors.toList());
	}

	/**
	 * Returns a {@link Page} of {@link User}. Sorting is available
	 * @param page
	 * @param size
	 * @param sort
	 * @return
	 */
	public Page<UserDTO> getAllUsers(int page, int size, String[] sort) {

		List<Order> orders = new ArrayList<Order>();

		if (sort[0].contains(",")) {
			// will sort more than 2 fields
			// sortOrder="field, direction"
			for (String sortOrder : sort) {
				String[] _sort = sortOrder.split(",");
				orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
			}
		} else {
			// sort=[field, direction]
			orders.add(new Order(getSortDirection(sort[1]), sort[0]));
		}

		Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

		Page<UserEntity> pageWithUsers = userRepository.findByTenantId(TenantContext.getTenantId(), pagingSort);

		Page<UserDTO> pageWithUserDTO = pageWithUsers.map(user -> {
			UserDTO userDTO = new UserDTO();
			modelMapper.map(user, userDTO);
			return userDTO;
		});

		return pageWithUserDTO;
	}

	private Sort.Direction getSortDirection(String direction) {
		if (direction.equals("asc")) {
			return Sort.Direction.ASC;
		} else if (direction.equals("desc")) {
			return Sort.Direction.DESC;
		}

		return Sort.Direction.ASC;
	}
}
