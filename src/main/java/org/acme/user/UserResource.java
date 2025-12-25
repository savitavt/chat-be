package org.acme.user;

import org.eclipse.microprofile.graphql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@GraphQLApi
public class UserResource {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    // --- CREATE ---
    @Mutation
    public User createUser(UserInput input) {
        String id = UUID.randomUUID().toString();
        User user = new User(id, input.name(), input.email());
        users.put(id, user);
        return user;
    }

    // --- READ ---
    @Query
    public List<User> allUsers() {
        return new ArrayList<>(users.values());
    }

    @Query
    public User getUser(String id) {
        return users.get(id);
    }

    @Query("getUsersByName")
    public List<User> getUsersByName(@Name("name") String name) {
        return users.values().stream()
                .filter(u -> u.name().equalsIgnoreCase(name))
                .toList();
    }

    // --- UPDATE ---
    @Mutation
    public User updateUser(String id, UserInput input) {
        User existing = users.get(id);
        if (existing == null) return null;
        // Create new record with updated fields
        User updated = new User(
                existing.id(),
                input.name() != null ? input.name() : existing.name(),
                input.email() != null ? input.email() : existing.email()
        );
        users.put(id, updated);
        return updated;
    }

    // --- DELETE ---
    @Mutation
    public boolean deleteUser(String id) {
        return users.remove(id) != null;
    }
}

