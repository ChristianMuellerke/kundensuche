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

import de.cmuellerke.demo.data.dto.UserDTO;
import de.cmuellerke.demo.data.entity.UserDAO;
import de.cmuellerke.demo.repository.UserRepository;
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

//	public UserDTO create(UserDTO user) {
//		return convertToDto(userRepository.save(convertToEntity(user)));
//	}

	public UserDTO update(Long id, UserDTO user) {
		UserDAO userFromRepo = userRepository.getById(id);
		modelMapper.map(user, userFromRepo);
		return convertToDto(userRepository.save(userFromRepo));
	}

	public void delete(Long userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*
		 * TODO: Das hier weitermachen
		 */
		if ("javainuse".equals(username)) {
			log.debug("Testuser is used, thats not ok");
			return new User("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
					new ArrayList<>());
		} else {
			log.debug("loading user {} from db", username);
			Optional<UserDAO> userFromDB = userRepository.findByUserName(username);
			
			return userFromDB.map(user -> {
				return new User(user.getUserName(), user.getPassword(), new ArrayList<>());
			})
			.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		}
	}

	public UserDTO save(UserDTO user) {
		UserDAO newUser = new UserDAO();
		newUser.setUserName(user.getUserName());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
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
