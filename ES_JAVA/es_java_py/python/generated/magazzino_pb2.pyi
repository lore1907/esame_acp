from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Optional as _Optional

DESCRIPTOR: _descriptor.FileDescriptor

class Articolo(_message.Message):
    __slots__ = ("valore",)
    VALORE_FIELD_NUMBER: _ClassVar[int]
    valore: int
    def __init__(self, valore: _Optional[int] = ...) -> None: ...

class Empty(_message.Message):
    __slots__ = ()
    def __init__(self) -> None: ...
