# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: command.proto

from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)


import table_pb2


DESCRIPTOR = _descriptor.FileDescriptor(
  name='command.proto',
  package='knime',
  serialized_pb='\n\rcommand.proto\x12\x05knime\x1a\x0btable.proto\"\xd9\n\n\x07\x43ommand\x12\'\n\x07\x65xecute\x18\x01 \x01(\x0b\x32\x16.knime.Command.Execute\x12\x39\n\x10putFlowVariables\x18\x02 \x01(\x0b\x32\x1f.knime.Command.PutFlowVariables\x12)\n\x08putTable\x18\x03 \x01(\x0b\x32\x17.knime.Command.PutTable\x12\x33\n\rappendToTable\x18\x04 \x01(\x0b\x32\x1c.knime.Command.AppendToTable\x12)\n\x08getTable\x18\x05 \x01(\x0b\x32\x17.knime.Command.GetTable\x12\x33\n\rlistVariables\x18\x06 \x01(\x0b\x32\x1c.knime.Command.ListVariables\x12#\n\x05reset\x18\x07 \x01(\x0b\x32\x14.knime.Command.Reset\x12\x37\n\x0fhasAutoComplete\x18\x08 \x01(\x0b\x32\x1e.knime.Command.HasAutoComplete\x12\x31\n\x0c\x61utoComplete\x18\t \x01(\x0b\x32\x1b.knime.Command.AutoComplete\x12)\n\x08getImage\x18\n \x01(\x0b\x32\x17.knime.Command.GetImage\x12+\n\tgetObject\x18\x0b \x01(\x0b\x32\x18.knime.Command.GetObject\x12+\n\tputObject\x18\x0c \x01(\x0b\x32\x18.knime.Command.PutObject\x1a\x1d\n\x07\x45xecute\x12\x12\n\nsourceCode\x18\x01 \x02(\t\x1a\x84\x03\n\x10PutFlowVariables\x12\x0b\n\x03key\x18\x01 \x02(\t\x12H\n\x0fintegerVariable\x18\x02 \x03(\x0b\x32/.knime.Command.PutFlowVariables.IntegerVariable\x12\x46\n\x0e\x64oubleVariable\x18\x03 \x03(\x0b\x32..knime.Command.PutFlowVariables.DoubleVariable\x12\x46\n\x0estringVariable\x18\x04 \x03(\x0b\x32..knime.Command.PutFlowVariables.StringVariable\x1a-\n\x0fIntegerVariable\x12\x0b\n\x03key\x18\x01 \x02(\t\x12\r\n\x05value\x18\x02 \x02(\x05\x1a,\n\x0e\x44oubleVariable\x12\x0b\n\x03key\x18\x01 \x02(\t\x12\r\n\x05value\x18\x02 \x02(\x01\x1a,\n\x0eStringVariable\x12\x0b\n\x03key\x18\x01 \x02(\t\x12\r\n\x05value\x18\x02 \x02(\t\x1a\x34\n\x08PutTable\x12\x0b\n\x03key\x18\x01 \x02(\t\x12\x1b\n\x05table\x18\x02 \x02(\x0b\x32\x0c.knime.Table\x1a\x39\n\rAppendToTable\x12\x0b\n\x03key\x18\x01 \x02(\t\x12\x1b\n\x05table\x18\x02 \x02(\x0b\x32\x0c.knime.Table\x1a*\n\x08GetTable\x12\x0b\n\x03key\x18\x01 \x02(\t\x12\x11\n\tchunkSize\x18\x02 \x02(\x05\x1a\x0f\n\rListVariables\x1a\x07\n\x05Reset\x1a\x11\n\x0fHasAutoComplete\x1a@\n\x0c\x41utoComplete\x12\x12\n\nsourceCode\x18\x01 \x02(\t\x12\x0c\n\x04line\x18\x02 \x02(\x05\x12\x0e\n\x06\x63olumn\x18\x03 \x02(\x05\x1a\x17\n\x08GetImage\x12\x0b\n\x03key\x18\x01 \x02(\t\x1a\x18\n\tGetObject\x12\x0b\n\x03key\x18\x01 \x02(\t\x1a/\n\tPutObject\x12\x0b\n\x03key\x18\x01 \x02(\t\x12\x15\n\rpickledObject\x18\x02 \x02(\tB<\n\x1dorg.knime.python.kernel.protoB\x1bProtobufPythonKernelCommand')




_COMMAND_EXECUTE = _descriptor.Descriptor(
  name='Execute',
  full_name='knime.Command.Execute',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='sourceCode', full_name='knime.Command.Execute.sourceCode', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=619,
  serialized_end=648,
)

_COMMAND_PUTFLOWVARIABLES_INTEGERVARIABLE = _descriptor.Descriptor(
  name='IntegerVariable',
  full_name='knime.Command.PutFlowVariables.IntegerVariable',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='key', full_name='knime.Command.PutFlowVariables.IntegerVariable.key', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='value', full_name='knime.Command.PutFlowVariables.IntegerVariable.value', index=1,
      number=2, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=902,
  serialized_end=947,
)

_COMMAND_PUTFLOWVARIABLES_DOUBLEVARIABLE = _descriptor.Descriptor(
  name='DoubleVariable',
  full_name='knime.Command.PutFlowVariables.DoubleVariable',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='key', full_name='knime.Command.PutFlowVariables.DoubleVariable.key', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='value', full_name='knime.Command.PutFlowVariables.DoubleVariable.value', index=1,
      number=2, type=1, cpp_type=5, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=949,
  serialized_end=993,
)

_COMMAND_PUTFLOWVARIABLES_STRINGVARIABLE = _descriptor.Descriptor(
  name='StringVariable',
  full_name='knime.Command.PutFlowVariables.StringVariable',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='key', full_name='knime.Command.PutFlowVariables.StringVariable.key', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='value', full_name='knime.Command.PutFlowVariables.StringVariable.value', index=1,
      number=2, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=995,
  serialized_end=1039,
)

_COMMAND_PUTFLOWVARIABLES = _descriptor.Descriptor(
  name='PutFlowVariables',
  full_name='knime.Command.PutFlowVariables',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='key', full_name='knime.Command.PutFlowVariables.key', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='integerVariable', full_name='knime.Command.PutFlowVariables.integerVariable', index=1,
      number=2, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='doubleVariable', full_name='knime.Command.PutFlowVariables.doubleVariable', index=2,
      number=3, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='stringVariable', full_name='knime.Command.PutFlowVariables.stringVariable', index=3,
      number=4, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[_COMMAND_PUTFLOWVARIABLES_INTEGERVARIABLE, _COMMAND_PUTFLOWVARIABLES_DOUBLEVARIABLE, _COMMAND_PUTFLOWVARIABLES_STRINGVARIABLE, ],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=651,
  serialized_end=1039,
)

_COMMAND_PUTTABLE = _descriptor.Descriptor(
  name='PutTable',
  full_name='knime.Command.PutTable',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='key', full_name='knime.Command.PutTable.key', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='table', full_name='knime.Command.PutTable.table', index=1,
      number=2, type=11, cpp_type=10, label=2,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=1041,
  serialized_end=1093,
)

_COMMAND_APPENDTOTABLE = _descriptor.Descriptor(
  name='AppendToTable',
  full_name='knime.Command.AppendToTable',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='key', full_name='knime.Command.AppendToTable.key', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='table', full_name='knime.Command.AppendToTable.table', index=1,
      number=2, type=11, cpp_type=10, label=2,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=1095,
  serialized_end=1152,
)

_COMMAND_GETTABLE = _descriptor.Descriptor(
  name='GetTable',
  full_name='knime.Command.GetTable',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='key', full_name='knime.Command.GetTable.key', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='chunkSize', full_name='knime.Command.GetTable.chunkSize', index=1,
      number=2, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=1154,
  serialized_end=1196,
)

_COMMAND_LISTVARIABLES = _descriptor.Descriptor(
  name='ListVariables',
  full_name='knime.Command.ListVariables',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=1198,
  serialized_end=1213,
)

_COMMAND_RESET = _descriptor.Descriptor(
  name='Reset',
  full_name='knime.Command.Reset',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=1215,
  serialized_end=1222,
)

_COMMAND_HASAUTOCOMPLETE = _descriptor.Descriptor(
  name='HasAutoComplete',
  full_name='knime.Command.HasAutoComplete',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=1224,
  serialized_end=1241,
)

_COMMAND_AUTOCOMPLETE = _descriptor.Descriptor(
  name='AutoComplete',
  full_name='knime.Command.AutoComplete',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='sourceCode', full_name='knime.Command.AutoComplete.sourceCode', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='line', full_name='knime.Command.AutoComplete.line', index=1,
      number=2, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='column', full_name='knime.Command.AutoComplete.column', index=2,
      number=3, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=1243,
  serialized_end=1307,
)

_COMMAND_GETIMAGE = _descriptor.Descriptor(
  name='GetImage',
  full_name='knime.Command.GetImage',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='key', full_name='knime.Command.GetImage.key', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=1309,
  serialized_end=1332,
)

_COMMAND_GETOBJECT = _descriptor.Descriptor(
  name='GetObject',
  full_name='knime.Command.GetObject',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='key', full_name='knime.Command.GetObject.key', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=1334,
  serialized_end=1358,
)

_COMMAND_PUTOBJECT = _descriptor.Descriptor(
  name='PutObject',
  full_name='knime.Command.PutObject',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='key', full_name='knime.Command.PutObject.key', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='pickledObject', full_name='knime.Command.PutObject.pickledObject', index=1,
      number=2, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=1360,
  serialized_end=1407,
)

_COMMAND = _descriptor.Descriptor(
  name='Command',
  full_name='knime.Command',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='execute', full_name='knime.Command.execute', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='putFlowVariables', full_name='knime.Command.putFlowVariables', index=1,
      number=2, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='putTable', full_name='knime.Command.putTable', index=2,
      number=3, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='appendToTable', full_name='knime.Command.appendToTable', index=3,
      number=4, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='getTable', full_name='knime.Command.getTable', index=4,
      number=5, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='listVariables', full_name='knime.Command.listVariables', index=5,
      number=6, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='reset', full_name='knime.Command.reset', index=6,
      number=7, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='hasAutoComplete', full_name='knime.Command.hasAutoComplete', index=7,
      number=8, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='autoComplete', full_name='knime.Command.autoComplete', index=8,
      number=9, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='getImage', full_name='knime.Command.getImage', index=9,
      number=10, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='getObject', full_name='knime.Command.getObject', index=10,
      number=11, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='putObject', full_name='knime.Command.putObject', index=11,
      number=12, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[_COMMAND_EXECUTE, _COMMAND_PUTFLOWVARIABLES, _COMMAND_PUTTABLE, _COMMAND_APPENDTOTABLE, _COMMAND_GETTABLE, _COMMAND_LISTVARIABLES, _COMMAND_RESET, _COMMAND_HASAUTOCOMPLETE, _COMMAND_AUTOCOMPLETE, _COMMAND_GETIMAGE, _COMMAND_GETOBJECT, _COMMAND_PUTOBJECT, ],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=38,
  serialized_end=1407,
)

_COMMAND_EXECUTE.containing_type = _COMMAND;
_COMMAND_PUTFLOWVARIABLES_INTEGERVARIABLE.containing_type = _COMMAND_PUTFLOWVARIABLES;
_COMMAND_PUTFLOWVARIABLES_DOUBLEVARIABLE.containing_type = _COMMAND_PUTFLOWVARIABLES;
_COMMAND_PUTFLOWVARIABLES_STRINGVARIABLE.containing_type = _COMMAND_PUTFLOWVARIABLES;
_COMMAND_PUTFLOWVARIABLES.fields_by_name['integerVariable'].message_type = _COMMAND_PUTFLOWVARIABLES_INTEGERVARIABLE
_COMMAND_PUTFLOWVARIABLES.fields_by_name['doubleVariable'].message_type = _COMMAND_PUTFLOWVARIABLES_DOUBLEVARIABLE
_COMMAND_PUTFLOWVARIABLES.fields_by_name['stringVariable'].message_type = _COMMAND_PUTFLOWVARIABLES_STRINGVARIABLE
_COMMAND_PUTFLOWVARIABLES.containing_type = _COMMAND;
_COMMAND_PUTTABLE.fields_by_name['table'].message_type = table_pb2._TABLE
_COMMAND_PUTTABLE.containing_type = _COMMAND;
_COMMAND_APPENDTOTABLE.fields_by_name['table'].message_type = table_pb2._TABLE
_COMMAND_APPENDTOTABLE.containing_type = _COMMAND;
_COMMAND_GETTABLE.containing_type = _COMMAND;
_COMMAND_LISTVARIABLES.containing_type = _COMMAND;
_COMMAND_RESET.containing_type = _COMMAND;
_COMMAND_HASAUTOCOMPLETE.containing_type = _COMMAND;
_COMMAND_AUTOCOMPLETE.containing_type = _COMMAND;
_COMMAND_GETIMAGE.containing_type = _COMMAND;
_COMMAND_GETOBJECT.containing_type = _COMMAND;
_COMMAND_PUTOBJECT.containing_type = _COMMAND;
_COMMAND.fields_by_name['execute'].message_type = _COMMAND_EXECUTE
_COMMAND.fields_by_name['putFlowVariables'].message_type = _COMMAND_PUTFLOWVARIABLES
_COMMAND.fields_by_name['putTable'].message_type = _COMMAND_PUTTABLE
_COMMAND.fields_by_name['appendToTable'].message_type = _COMMAND_APPENDTOTABLE
_COMMAND.fields_by_name['getTable'].message_type = _COMMAND_GETTABLE
_COMMAND.fields_by_name['listVariables'].message_type = _COMMAND_LISTVARIABLES
_COMMAND.fields_by_name['reset'].message_type = _COMMAND_RESET
_COMMAND.fields_by_name['hasAutoComplete'].message_type = _COMMAND_HASAUTOCOMPLETE
_COMMAND.fields_by_name['autoComplete'].message_type = _COMMAND_AUTOCOMPLETE
_COMMAND.fields_by_name['getImage'].message_type = _COMMAND_GETIMAGE
_COMMAND.fields_by_name['getObject'].message_type = _COMMAND_GETOBJECT
_COMMAND.fields_by_name['putObject'].message_type = _COMMAND_PUTOBJECT
DESCRIPTOR.message_types_by_name['Command'] = _COMMAND

class Command(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType

  class Execute(_message.Message):
    __metaclass__ = _reflection.GeneratedProtocolMessageType
    DESCRIPTOR = _COMMAND_EXECUTE

    # @@protoc_insertion_point(class_scope:knime.Command.Execute)

  class PutFlowVariables(_message.Message):
    __metaclass__ = _reflection.GeneratedProtocolMessageType

    class IntegerVariable(_message.Message):
      __metaclass__ = _reflection.GeneratedProtocolMessageType
      DESCRIPTOR = _COMMAND_PUTFLOWVARIABLES_INTEGERVARIABLE

      # @@protoc_insertion_point(class_scope:knime.Command.PutFlowVariables.IntegerVariable)

    class DoubleVariable(_message.Message):
      __metaclass__ = _reflection.GeneratedProtocolMessageType
      DESCRIPTOR = _COMMAND_PUTFLOWVARIABLES_DOUBLEVARIABLE

      # @@protoc_insertion_point(class_scope:knime.Command.PutFlowVariables.DoubleVariable)

    class StringVariable(_message.Message):
      __metaclass__ = _reflection.GeneratedProtocolMessageType
      DESCRIPTOR = _COMMAND_PUTFLOWVARIABLES_STRINGVARIABLE

      # @@protoc_insertion_point(class_scope:knime.Command.PutFlowVariables.StringVariable)
    DESCRIPTOR = _COMMAND_PUTFLOWVARIABLES

    # @@protoc_insertion_point(class_scope:knime.Command.PutFlowVariables)

  class PutTable(_message.Message):
    __metaclass__ = _reflection.GeneratedProtocolMessageType
    DESCRIPTOR = _COMMAND_PUTTABLE

    # @@protoc_insertion_point(class_scope:knime.Command.PutTable)

  class AppendToTable(_message.Message):
    __metaclass__ = _reflection.GeneratedProtocolMessageType
    DESCRIPTOR = _COMMAND_APPENDTOTABLE

    # @@protoc_insertion_point(class_scope:knime.Command.AppendToTable)

  class GetTable(_message.Message):
    __metaclass__ = _reflection.GeneratedProtocolMessageType
    DESCRIPTOR = _COMMAND_GETTABLE

    # @@protoc_insertion_point(class_scope:knime.Command.GetTable)

  class ListVariables(_message.Message):
    __metaclass__ = _reflection.GeneratedProtocolMessageType
    DESCRIPTOR = _COMMAND_LISTVARIABLES

    # @@protoc_insertion_point(class_scope:knime.Command.ListVariables)

  class Reset(_message.Message):
    __metaclass__ = _reflection.GeneratedProtocolMessageType
    DESCRIPTOR = _COMMAND_RESET

    # @@protoc_insertion_point(class_scope:knime.Command.Reset)

  class HasAutoComplete(_message.Message):
    __metaclass__ = _reflection.GeneratedProtocolMessageType
    DESCRIPTOR = _COMMAND_HASAUTOCOMPLETE

    # @@protoc_insertion_point(class_scope:knime.Command.HasAutoComplete)

  class AutoComplete(_message.Message):
    __metaclass__ = _reflection.GeneratedProtocolMessageType
    DESCRIPTOR = _COMMAND_AUTOCOMPLETE

    # @@protoc_insertion_point(class_scope:knime.Command.AutoComplete)

  class GetImage(_message.Message):
    __metaclass__ = _reflection.GeneratedProtocolMessageType
    DESCRIPTOR = _COMMAND_GETIMAGE

    # @@protoc_insertion_point(class_scope:knime.Command.GetImage)

  class GetObject(_message.Message):
    __metaclass__ = _reflection.GeneratedProtocolMessageType
    DESCRIPTOR = _COMMAND_GETOBJECT

    # @@protoc_insertion_point(class_scope:knime.Command.GetObject)

  class PutObject(_message.Message):
    __metaclass__ = _reflection.GeneratedProtocolMessageType
    DESCRIPTOR = _COMMAND_PUTOBJECT

    # @@protoc_insertion_point(class_scope:knime.Command.PutObject)
  DESCRIPTOR = _COMMAND

  # @@protoc_insertion_point(class_scope:knime.Command)


DESCRIPTOR.has_options = True
DESCRIPTOR._options = _descriptor._ParseOptions(descriptor_pb2.FileOptions(), '\n\035org.knime.python.kernel.protoB\033ProtobufPythonKernelCommand')
# @@protoc_insertion_point(module_scope)
