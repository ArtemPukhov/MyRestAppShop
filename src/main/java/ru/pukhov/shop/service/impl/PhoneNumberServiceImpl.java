package ru.pukhov.shop.service.impl;


import ru.pukhov.shop.exception.NotFoundException;
import ru.pukhov.shop.model.PhoneNumber;
import ru.pukhov.shop.repository.PhoneNumberRepository;
import ru.pukhov.shop.repository.impl.PhoneNumberRepositoryImpl;
import ru.pukhov.shop.service.PhoneNumberService;
import ru.pukhov.shop.servlet.dto.PhoneNumberIncomingDto;
import ru.pukhov.shop.servlet.dto.PhoneNumberOutGoingDto;
import ru.pukhov.shop.servlet.dto.PhoneNumberUpdateDto;
import ru.pukhov.shop.servlet.mapper.PhoneNumberDtoMapper;
import ru.pukhov.shop.servlet.mapper.impl.PhoneNumberDtoMapperImpl;

import java.util.List;

public class PhoneNumberServiceImpl implements PhoneNumberService {
    private final PhoneNumberDtoMapper phoneNumberDtoMapper = PhoneNumberDtoMapperImpl.getInstance();
    private static PhoneNumberService instance;
    private final PhoneNumberRepository phoneNumberRepository = PhoneNumberRepositoryImpl.getInstance();


    private PhoneNumberServiceImpl() {
    }

    public static synchronized PhoneNumberService getInstance() {
        if (instance == null) {
            instance = new PhoneNumberServiceImpl();
        }
        return instance;
    }

    @Override
    public PhoneNumberOutGoingDto save(PhoneNumberIncomingDto phoneNumberDto) {
        PhoneNumber phoneNumber = phoneNumberDtoMapper.map(phoneNumberDto);
        phoneNumber = phoneNumberRepository.save(phoneNumber);
        return phoneNumberDtoMapper.map(phoneNumber);
    }

    @Override
    public void update(PhoneNumberUpdateDto phoneNumberUpdateDto) throws NotFoundException {
        if (phoneNumberRepository.existById(phoneNumberUpdateDto.getId())) {
            PhoneNumber phoneNumber = phoneNumberDtoMapper.map(phoneNumberUpdateDto);
            phoneNumberRepository.update(phoneNumber);
        } else {
            throw new NotFoundException("PhoneNumber not found.");
        }
    }

    @Override
    public PhoneNumberOutGoingDto findById(Long phoneNumberId) throws NotFoundException {
        PhoneNumber phoneNumber = phoneNumberRepository.findById(phoneNumberId).orElseThrow(() ->
                new NotFoundException("PhoneNumber not found."));
        return phoneNumberDtoMapper.map(phoneNumber);
    }

    @Override
    public List<PhoneNumberOutGoingDto> findAll() {
        List<PhoneNumber> phoneNumberList = phoneNumberRepository.findAll();
        return phoneNumberDtoMapper.map(phoneNumberList);
    }

    @Override
    public boolean delete(Long phoneNumberId) {
        return phoneNumberRepository.deleteById(phoneNumberId);
    }

}
