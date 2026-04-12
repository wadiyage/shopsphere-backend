package edu.icet.shopsphere.controller;

import edu.icet.shopsphere.dto.address.AddressRequest;
import edu.icet.shopsphere.dto.address.AddressResponse;
import edu.icet.shopsphere.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public List<AddressResponse> getMyAddresses() {
        return addressService.getMyAddresses();
    }

    @PostMapping
    public AddressResponse createAddress(@RequestBody AddressRequest request) {
        return addressService.createAddress(request);
    }

    @PutMapping("/{id}")
    public AddressResponse updateAddress(@PathVariable Long id, @RequestBody AddressRequest request) {
        return addressService.updateAddress(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
    }

    @PutMapping("/{id}/default")
    public void setDefaultAddress(@PathVariable Long id) {
        addressService.setDefaultAddress(id);
    }
}
