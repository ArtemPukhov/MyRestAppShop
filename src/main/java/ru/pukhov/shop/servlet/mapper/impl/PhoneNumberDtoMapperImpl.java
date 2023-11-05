package ru.pukhov.shop.servlet.mapper.impl;


import ru.pukhov.shop.model.PhoneNumber;
import ru.pukhov.shop.model.User;
import ru.pukhov.shop.servlet.dto.PhoneNumberIncomingDto;
import ru.pukhov.shop.servlet.dto.PhoneNumberOutGoingDto;
import ru.pukhov.shop.servlet.dto.PhoneNumberUpdateDto;
import ru.pukhov.shop.servlet.dto.UserSmallOutGoingDto;
import ru.pukhov.shop.servlet.mapper.PhoneNumberDtoMapper;

import java.util.List;

public class PhoneNumberDtoMapperImpl implements PhoneNumberDtoMapper {
    private static PhoneNumberDtoMapper instance;

    private PhoneNumberDtoMapperImpl() {
    }

    public static synchronized PhoneNumberDtoMapper getInstance() {
        if (instance == null) {
            instance = new PhoneNumberDtoMapperImpl();
        }
        return instance;
    }

    @Override
    public PhoneNumber map(PhoneNumberIncomingDto phoneDto) {
        return new PhoneNumber(
                null,
                phoneDto.getNumber(),
                null
        );
    }

    @Override
    public PhoneNumberOutGoingDto map(PhoneNumber phoneNumber) {
        return new PhoneNumberOutGoingDto(
                phoneNumber.getId(),
                phoneNumber.getNumber(),
                phoneNumber.getUser() == null ?
                        null :
                        new UserSmallOutGoingDto(
                                phoneNumber.getUser().getId(),
                                phoneNumber.getUser().getFirstName(),
                                phoneNumber.getUser().getLastName()
                        )
        );
    }

    @Override
    public List<PhoneNumberOutGoingDto> map(List<PhoneNumber> phoneNumberList) {
        return phoneNumberList.stream().map(this::map).toList();
    }

    @Override
    public List<PhoneNumber> mapUpdateList(List<PhoneNumberUpdateDto> phoneNumberUpdateList) {
        return phoneNumberUpdateList.stream().map(this::map).toList();
    }

    @Override
    public PhoneNumber map(PhoneNumberUpdateDto phoneDto) {
        return new PhoneNumber(
                phoneDto.getId(),
                phoneDto.getNumber(),
                new User(
                        phoneDto.getUserId(),
                        null,
                        null,
                        null,
                        List.of(),
                        List.of()
                )
        );
    }

}
