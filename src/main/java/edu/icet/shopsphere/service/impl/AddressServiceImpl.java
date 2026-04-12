package edu.icet.shopsphere.service.impl;

import edu.icet.shopsphere.dto.address.AddressRequest;
import edu.icet.shopsphere.dto.address.AddressResponse;
import edu.icet.shopsphere.entity.Address;
import edu.icet.shopsphere.entity.User;
import edu.icet.shopsphere.exception.ResourceNotFoundException;
import edu.icet.shopsphere.repository.AddressRepository;
import edu.icet.shopsphere.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public List<AddressResponse> getMyAddresses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        List<Address> addresses = addressRepository.findByUserId(user.getId());
        return addresses.stream().map(this::mapToResponse).toList();
    }

    private AddressResponse mapToResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .fullName(address.getFullName())
                .phone(address.getPhone())
                .addressLine(address.getAddressLine())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .isDefault(address.isDefault())
                .build();
    }

    @Override
    public AddressResponse createAddress(AddressRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Address address = Address.builder()
                .user(user)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .addressLine(request.getAddressLine())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .isDefault(request.isDefault())
                .build();

        List<Address> userAddresses = addressRepository.findByUserId(user.getId());

        if(userAddresses.isEmpty()) {
            address.setDefault(true);
        } else if(request.isDefault()) {
            userAddresses.forEach(address1 -> address1.setDefault(false));
            addressRepository.saveAll(userAddresses);
        }

        Address savedAddress = addressRepository.save(address);
        return mapToResponse(savedAddress);
    }

    @Override
    public AddressResponse updateAddress(Long id, AddressRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        if(!address.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Address not found with id: " + id);
        }

        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());

        if(request.isDefault()) {
            List<Address> userAddresses = addressRepository.findByUserId(user.getId());
            userAddresses.forEach(address1 -> address1.setDefault(false));
            addressRepository.saveAll(userAddresses);
        }
        address.setDefault(true);

        Address updatedAddress = addressRepository.save(address);
        return mapToResponse(updatedAddress);
    }

    @Override
    public void deleteAddress(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        if(!address.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Address not found with id: " + id);
        }

        if(address.isDefault()) {
            throw new IllegalStateException("Cannot delete default address. Please set another address as default before deleting this one.");
        } else {
            addressRepository.delete(address);

            List<Address> remaining = addressRepository.findByUserId(user.getId());
            if(!remaining.isEmpty()) {
                remaining.get(0).setDefault(true);
                addressRepository.save(remaining.get(0));
            }
        }
    }

    @Override
    public void setDefaultAddress(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        if(!address.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Address not found with id: " + id);
        }

        List<Address> userAddresses = addressRepository.findByUserId(user.getId());
        userAddresses.forEach(address1 -> address1.setDefault(address.getId().equals(id)));
        addressRepository.saveAll(userAddresses);
    }
}
