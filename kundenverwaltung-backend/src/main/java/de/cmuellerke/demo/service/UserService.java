package de.cmuellerke.demo.service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.cmuellerke.demo.data.dto.ApplicationUser;
import de.cmuellerke.demo.data.dto.UserDTO;
import de.cmuellerke.demo.data.entity.UserDAO;
import de.cmuellerke.demo.repository.UserRepository;
import de.cmuellerke.demo.tenancy.TenantContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Autowired
	private ModelMapper modelMapper;

	public UserDTO update(Long id, UserDTO user) {
		UserDAO userFromRepo = userRepository.getById(id);
		modelMapper.map(user, userFromRepo);
		return convertToDto(userRepository.save(userFromRepo));
	}

	public void delete(Long userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public ApplicationUser loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("TenantId = {}", getTenantId());
		
		if ("javainuse".equals(username)) {
			log.debug("Testuser is used, thats not ok");
			ApplicationUser user = new ApplicationUser("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6", getTenantId(),
					new ArrayList<>());
			return user;
		} else {
			log.debug("loading user {} from db", username);
			Optional<UserDAO> userFromDB = userRepository.findByUserNameAndTenantId(username, getTenantId());
			
			return userFromDB.map(user -> {
				return new ApplicationUser(user.getUserName(), user.getPassword(), user.getTenantId(), new ArrayList<>());
			})
			.orElseThrow(() -> new UsernameNotFoundException("[" + getTenantId() + "] User " + username + " not found."));
		}
	}

	private String getTenantId() {
		return TenantContext.getTenantId();
	}

	public UserDTO save(UserDTO user) {
		UserDAO newUser = new UserDAO();
		newUser.setUserName(user.getUserName());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setTenantId(user.getTenant());
		UserDAO savedUser = userRepository.save(newUser);
		return convertToDto(savedUser);
	}
	
	public Iterable<UserDTO> getAll() {
		return userRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public Page<UserDTO> getAllPaged(Pageable pageable) {
		Page<UserDAO> page = userRepository.findAll(pageable);
		return page.map(this::convertToDto);
	}

	public UserDTO getById(Long id) throws UserNotFoundException {
		Optional<UserDAO> queryResult = userRepository.findById(id);
		return queryResult.map(this::convertToDto).orElseThrow(() -> new UserNotFoundException());
	}

	private UserDTO convertToDto(UserDAO user) {
		UserDTO userDto = modelMapper.map(user, UserDTO.class);
		return userDto;
	}

	private UserDAO convertToEntity(UserDTO userDto) {
		UserDAO user = modelMapper.map(userDto, UserDAO.class);

		return user;
	}
}
