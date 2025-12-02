from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Optional as _Optional

DESCRIPTOR: _descriptor.FileDescriptor

class Empty(_message.Message):
    __slots__ = ()
    def __init__(self) -> None: ...

class Sensor(_message.Message):
    __slots__ = ("_id", "data_type")
    _ID_FIELD_NUMBER: _ClassVar[int]
    DATA_TYPE_FIELD_NUMBER: _ClassVar[int]
    _id: int
    data_type: str
    def __init__(self, _id: _Optional[int] = ..., data_type: _Optional[str] = ...) -> None: ...

class MeanRequest(_message.Message):
    __slots__ = ("sensor_id", "data_type")
    SENSOR_ID_FIELD_NUMBER: _ClassVar[int]
    DATA_TYPE_FIELD_NUMBER: _ClassVar[int]
    sensor_id: int
    data_type: str
    def __init__(self, sensor_id: _Optional[int] = ..., data_type: _Optional[str] = ...) -> None: ...

class StringMessage(_message.Message):
    __slots__ = ("value",)
    VALUE_FIELD_NUMBER: _ClassVar[int]
    value: str
    def __init__(self, value: _Optional[str] = ...) -> None: ...
