# call it using 'make NAME=name_of_code_file_without_extension'
NAME = "Main"

all:
	@echo "Compiling..."
	javac *.java

run: all
	@echo "Running..."
	java $(NAME)

clean:
	rm -rf *.class