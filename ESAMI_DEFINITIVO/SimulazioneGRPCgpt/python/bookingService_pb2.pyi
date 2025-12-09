from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Optional as _Optional

DESCRIPTOR: _descriptor.FileDescriptor

class Empty(_message.Message):
    __slots__ = ()
    def __init__(self) -> None: ...

class Reservation(_message.Message):
    __slots__ = ("user", "people", "timeSlot")
    USER_FIELD_NUMBER: _ClassVar[int]
    PEOPLE_FIELD_NUMBER: _ClassVar[int]
    TIMESLOT_FIELD_NUMBER: _ClassVar[int]
    user: str
    people: int
    timeSlot: str
    def __init__(self, user: _Optional[str] = ..., people: _Optional[int] = ..., timeSlot: _Optional[str] = ...) -> None: ...

class StatsResponse(_message.Message):
    __slots__ = ("total",)
    TOTAL_FIELD_NUMBER: _ClassVar[int]
    total: int
    def __init__(self, total: _Optional[int] = ...) -> None: ...
