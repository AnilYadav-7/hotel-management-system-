package com.hotel.guests.service;

import com.hotel.common.exception.ResourceNotFoundException;
import com.hotel.common.exception.DuplicateResourceException;
import com.hotel.guests.dto.GuestRequestDTO;
import com.hotel.guests.dto.GuestResponseDTO;
import com.hotel.guests.model.Guest;
import com.hotel.guests.repository.GuestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    public GuestServiceImpl(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Override
    public GuestResponseDTO addGuest(GuestRequestDTO request) throws ResourceNotFoundException {
        if (guestRepository.existsByPhone(request.getPhoneNumber())) {
            throw new DuplicateResourceException("Guest with phone " + request.getPhoneNumber() + " already exists!");
        }
        if (guestRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Guest with email " + request.getEmail() + " already exists!");
        }
        if (guestRepository.existsByIdProofNumber(request.getIdProofNumber())) {
            throw new DuplicateResourceException("Guest with ID Proof Number " + request.getIdProofNumber() + " already exists!");
        }
        Guest guest = new Guest();
        guest.setName(request.getName());
        guest.setEmail(request.getEmail());
        guest.setPhone(request.getPhoneNumber());
        guest.setIdProofNumber(request.getIdProofNumber());
        guest.setAddress(request.getAddress());

        Guest savedGuest = guestRepository.save(guest);
        return mapToDTO(savedGuest);
    }

    @Override
    public GuestResponseDTO getGuestById(Long id) throws ResourceNotFoundException {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with ID: " + id));
        return mapToDTO(guest);
    }

    @Override
    public GuestResponseDTO getGuestByPhone(String phone) throws ResourceNotFoundException {
        Guest guest = guestRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with Phone: " + phone));
        return mapToDTO(guest);
    }

    @Override
    public GuestResponseDTO updateGuest(Long id, GuestRequestDTO request) throws ResourceNotFoundException {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with ID: " + id));

        if (request.getName() != null) {
            guest.setName(request.getName());
        }
        if (request.getEmail() != null && !request.getEmail().equals(guest.getEmail())) {
            if (guestRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new DuplicateResourceException("Guest with email " + request.getEmail() + " already exists!");
            }
            guest.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(guest.getPhone())) {
            if (guestRepository.existsByPhone(request.getPhoneNumber())) {
                throw new DuplicateResourceException("Guest with phone " + request.getPhoneNumber() + " already exists!");
            }
            guest.setPhone(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            guest.setAddress(request.getAddress());
        }
        if (request.getIdProofNumber() != null) {
            guest.setIdProofNumber(request.getIdProofNumber());
        }

        return mapToDTO(guestRepository.save(guest));
    }

    @Override
    public void deleteGuest(Long id) throws ResourceNotFoundException {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with ID: " + id));
        guestRepository.delete(guest);
    }

    @Override
    public List<GuestResponseDTO> getAllGuests(int pageNo, int pageSize) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        Page<Guest> guestPage = guestRepository.findAll(pageable);
        return guestPage.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private GuestResponseDTO mapToDTO(Guest guest) {
        GuestResponseDTO dto = new GuestResponseDTO();
        dto.setId(guest.getId());
        dto.setName(guest.getName());
        dto.setEmail(guest.getEmail());
        dto.setPhoneNumber(guest.getPhone());
        dto.setIdProofNumber(guest.getIdProofNumber());
        dto.setAddress(guest.getAddress());
        return dto;
    }
}
