package ru.pukhov.shop.servlet.mapper;


import ru.pukhov.shop.model.PhoneNumber;
import ru.pukhov.shop.servlet.dto.PhoneNumberIncomingDto;
import ru.pukhov.shop.servlet.dto.PhoneNumberOutGoingDto;
import ru.pukhov.shop.servlet.dto.PhoneNumberUpdateDto;

import java.util.List;

public interface PhoneNumberDtoMapper {
    PhoneNumber map(PhoneNumberIncomingDto phoneNumberIncomingDto);

    PhoneNumberOutGoingDto map(PhoneNumber phoneNumber);

    List<PhoneNumberOutGoingDto> map(List<PhoneNumber> phoneNumberList);

    List<PhoneNumber> mapUpdateList(List<PhoneNumberUpdateDto> phoneNumberUpdateList);

    PhoneNumber map(PhoneNumberUpdateDto phoneNumberIncomingDto);
}
