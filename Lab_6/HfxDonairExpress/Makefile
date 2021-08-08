DIR  = build
HTMLDIR = docs
JC   = javac
DOCC = javadoc
OPTS = -g -d $(DIR) -cp $(DIR):junit-platform-console-standalone-1.7.1.jar
HTMLOPTS = -d $(HTMLDIR)

SRC     = Food.java HfxDonairExpress.java Menu.java UnitTests.java
CLASSES = $(patsubst %.java, $(DIR)/%.class, $(SRC) )
DOCS    = $(patsubst %.java, $(HTMLDIR)/%.html, $(SRC) )
TARGET  = hfxdonairexpress.jar

# Default target
all: $(TARGET)

# Class files
$(DIR)/%.class: %.java
	$(JC) $(OPTS) $<

# Jar
$(TARGET): $(CLASSES)
	jar -cfm hfxdonairexpress.jar manifest.mf -C $(DIR) .

# Documentation files
$(HTMLDIR)/%.html: %.java
	$(DOCC) $(HTMLOPTS) $<

# Documentation
doc: $(DOCS)

# Test
test: $(TARGET)
	java -jar junit-platform-console-standalone-1.7.1.jar --class-path $(DIR) --scan-class-path

# Clean
clean:
	$(RM) $(DIR)/*.class
	$(RM) $(HTMLDIR)/*.html