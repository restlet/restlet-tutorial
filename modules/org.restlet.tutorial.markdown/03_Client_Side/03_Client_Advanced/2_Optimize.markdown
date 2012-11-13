## Optimize your Restlet server applications ##

This section describes some hints to optimize Restlet server applications.

### Using caching ###

One possible optimization is not to serve related resources (like images, css...) when loading
a particular resource with HTML content. An approach can be to use cache support provided by
HTTP.

We describe here how to apply browser caching for all static elements loaded from a path with
a subfolder called nocache. For these elements, headers for caching will be automatically added.
For others, an expiration date of one month will be specified in headers.

This feature can be simply added with Restlet using filters within the method createInbountRoot of
your application class. A filter containing caching stuff can to be added in front of the Restlet
Directory that serves static content, as described below:

    router.attach("/static", new Filter(getContext(), new Directory(
                getContext(), (...))) {
        protected void afterHandle(Request request, Response response) {
            super.afterHandle(request, response);
            [adding caching stuff here]
        }
     });

Once the filter is added in the processing chain, we have to handle caching headers based on the
Representation and Response objects. The noCache method of the Response automatically adds the
related headers for no cache. For expiration date, the setExpirationDate method of the
Representation allows defining the laps of time before reloading the element content. Following
code describes the complete code:

    router.attach("/static", new Filter(getContext(), new Directory(
                getContext(), (...))) {
        protected void afterHandle(Request request, Response response) {
            super.afterHandle(request, response);
            if (response.getEntity() != null) {
                if (request.getResourceRef().toString(false, false)
                        .contains("nocache")) {
                    response.getEntity().setModificationDate(null);
                    response.getEntity().setExpirationDate(null);
                    response.getEntity().setTag(null);
                    response.getCacheDirectives().add(
                                CacheDirective.noCache());
                } else {
                    response.setStatus(Status.SUCCESS_OK);
                    Calendar c = new GregorianCalendar();
                    c.setTime(new Date());
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    response.getEntity().setExpirationDate(c.getTime());
                    response.getEntity().setModificationDate(null);
                }
            }
        }
     });

### Compressing content ###

Modern browsers support compression for received content. This allows to reduce payload of exchanged data.
Restlet supports this feature for server-side application using the Encoder class. The latter can take place
within the processing chain like router, authenticator and filter. You simply need to configure it within
the method createInbountRoot of your application class, as described below:

    Encoder encoder = new Encoder(getContext(), false, true, getEncoderService());
    encoder.setNext(router);
    return encoder;

### Configuring specific converters ###

Example of Jackson converter.

    private JacksonConverter getRegisteredJacksonConverter() {
        JacksonConverter jacksonConverter = null;
        List<ConverterHelper> converters = Engine.getInstance()
                .getRegisteredConverters();
        for (ConverterHelper converterHelper : converters) {
            System.out.println(converterHelper.getClass());
            if (converterHelper instanceof JacksonConverter) {
                jacksonConverter = (JacksonConverter) converterHelper;
                break;
            }
        }
    }

Getting the ObjectMapper and configures it for data mapping.

    private void configureJacksonConverter() {
        JacksonConverter jacksonConverter = getRegisteredJacksonConverter();

        if (jacksonConverter != null) {
            ObjectMapper objectMapper = jacksonConverter.getObjectMapper();
            objectMapper.configure(
                    SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            // objectMapper.configure(DeserializationFeature.WRITE_DATES_AS_TIMESTAMPS,
            // false);
            objectMapper.getDeserializationConfig().with(
                    new SimpleDateFormat("yyyy-MM-dd"));
        }
    }


