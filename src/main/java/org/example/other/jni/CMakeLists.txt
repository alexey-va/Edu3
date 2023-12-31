cmake_minimum_required(VERSION 3.15)
project(NativeLib)

# Specify the C++ standard
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

include_directories("C:/Program Files/BellSoft/LibericaJDK-21/include")
include_directories("C:/Program Files/BellSoft/LibericaJDK-21/include/win32")

# Add your source files here
set(SOURCE_FILES
        org_example_other_jni_TestLib.cpp
)

# Create a shared library from the source file
add_library(YourLibrary SHARED ${SOURCE_FILES})

# Set the output directory for the library
set_target_properties(YourLibrary PROPERTIES LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR})
set_target_properties(YourLibrary PROPERTIES OUTPUT_NAME native)
if(WIN32)
    set_target_properties(YourLibrary PROPERTIES PREFIX "")
endif()

add_custom_command(TARGET YourLibrary POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy $<TARGET_FILE:YourLibrary> ${CMAKE_CURRENT_SOURCE_DIR}/../../../../../resources/native.dll
)

add_custom_target(MoveLibrary ALL DEPENDS YourLibrary)