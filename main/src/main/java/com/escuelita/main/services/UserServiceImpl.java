package com.escuelita.main.services;

import com.escuelita.main.controllers.dtos.requests.CreateUserRequest;
import com.escuelita.main.controllers.dtos.requests.UpdateUserRequest;
import com.escuelita.main.controllers.dtos.responses.GetUserResponse;
import com.escuelita.main.entities.User;
import com.escuelita.main.repositories.IUserRepository;
import com.escuelita.main.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service("company")
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository repository;

    @Override
    public GetUserResponse get(Long id) {
        return from(id);
    }

    @Override
    public List<GetUserResponse> list() {
        return repository
                .findAll()
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
        /*List<GetUserResponse> responses = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);

            GetUserResponse response = new GetUserResponse();
            response.setId(user.getId());
            response.setEmail(user.getEmail());

            responses.add(response);
        }

        return responses;*/
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public GetUserResponse create(CreateUserRequest request) {
        User user = from(request);
        return from(repository.save(user));
    }

    @Override
    public GetUserResponse update(Long id, UpdateUserRequest request) {
        User user = repository.findById(id).orElseThrow(() -> new RuntimeException("The user does not exist"));
        user = update(user, request);
        return from(user);
    }

    private User update(User user, UpdateUserRequest request) {
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return repository.save(user);
    }

    private User from(CreateUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }

    private GetUserResponse from(User user) {
        GetUserResponse response = new GetUserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        return response;
    }

    private GetUserResponse from(Long idUser) {
        return repository.findById(idUser)
                .map(this::from)
                .orElseThrow(() -> new RuntimeException("The user does not exist"));
    }
}
