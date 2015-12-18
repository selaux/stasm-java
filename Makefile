LIBRARY := src/libstasm-jni.so
OBJFILES := src/Stasm.o
INCLUDES := -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux
CXXFLAGS := $(INCLUDES) -fpic

java:
	javac -classpath /usr/share/opencv/java/opencv-2412.jar src/org/selaux/stasm/Stasm.java
	javah -classpath /usr/share/opencv/java/opencv-2412.jar:./src -d src/ org.selaux.stasm.Stasm
	jar cf src/libstasm.jar -C src org/selaux/stasm/Stasm.class

$(LIBRARY): $(OBJFILES)
		$(CXX) -shared -lopencv_core -lstasm -o $@ $^

all:
	make java
	make $(LIBRARY)

clean:
	rm src/*.h
	rm src/*.o
	rm src/*.so
	rm src/*.jar
