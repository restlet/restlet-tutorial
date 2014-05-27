# Changes log

## Next version 

* Add ```self``` (instead of ```href```) field in representations
* Make trailing slash optional ( ```/contacts/``` or ```/contacts```)
* Remove Representation without ```self``` field
* Remove APISpark instrospector 
* Use RF 2.2.1

## RFApi140514 (05/14/14)

* WADL moved to branch wadl
* Use introspector to generate APISpark documentation : org.restlet.ext.apispark jar added
  * Generated documentation available [here](https://apispark.com/apis/1527/versions/1/overview/)
  * Use RF 2.3 M2
* In CompanyRepresentation & ContactRepresentation : add href field
* Remove DUNS as id for Company -> create an autoincremented id
* Rename Representations
* Use : 
  * POST to create, PUT to modify companies
  * PUT to create & modify contacts
* Add logging
