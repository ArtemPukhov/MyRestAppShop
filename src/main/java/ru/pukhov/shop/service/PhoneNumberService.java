package ru.pukhov.shop.service;


import ru.pukhov.shop.exception.NotFoundException;
import ru.pukhov.shop.servlet.dto.PhoneNumberIncomingDto;
import ru.pukhov.shop.servlet.dto.PhoneNumberOutGoingDto;
import ru.pukhov.shop.servlet.dto.PhoneNumberUpdateDto;

import java.util.List;

public interface PhoneNumberService {
    PhoneNumberOutGoingDto save(PhoneNumberIncomingDto phoneNumber);

    void update(PhoneNumberUpdateDto phoneNumber) throws NotFoundException;

    PhoneNumberOutGoingDto findById(Long phoneNumberId) throws NotFoundException;

    List<PhoneNumberOutGoingDto> findAll();

    boolean delete(Long phoneNumberId);
}
