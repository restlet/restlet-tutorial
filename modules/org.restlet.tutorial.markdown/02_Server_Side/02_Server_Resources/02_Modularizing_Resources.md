# Modularizing resources #

DRY (Don't Repeat Yourself (see http://en.wikipedia.org/wiki/DRY) is a common principle of sofware development that
aimed at reducing repetition of information of all kinds. In this section, we will focus how aspects that helps
to improve resource processing.

## Using generics ##

Restlet supports generics to modularize common processing. Let's tackle such aspects when implementing
CRUD processing for resources. As described previously in section XX, "", we implement list / add / update /
delete operations based on two Restlet server resources:

* The one that handles element list and adding
* The one that handles particular element


