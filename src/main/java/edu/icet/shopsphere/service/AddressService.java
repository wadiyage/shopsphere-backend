package edu.icet.shopsphere.service;

import edu.icet.shopsphere.dto.address.AddressRequest;
import edu.icet.shopsphere.dto.address.AddressResponse;

import java.util.List;

public interface AddressService {
    List<AddressResponse> getMyAddresses();
    AddressResponse createAddress(AddressRequest request);
    AddressResponse updateAddress(Long id, AddressRequest request);
    void deleteAddress(Long id);
    void setDefaultAddress(Long id);
}
