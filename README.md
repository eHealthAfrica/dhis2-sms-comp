# dhis2-sms-comp

A java library used to compress SMS submissions sent to DHIS2

## Overview

DHIS2 (District Health Information System) is open source software used for recording and analysing public health information. Submissions can be made to the DHIS2 server via SMS, which is especially handy in areas with low internet connectivity. Unfortunately, large submissions, such as those for Aggregate Data Sets, can sometimes be several SMSs. This can be costly for the sender and can cause technical issues if any one of the SMS fails to send due to poor signal.

For that reason, this library was created, so that a typical DHIS2 SMS submission could be compressed down to the smallest possible size. It does this by converting the plain text submission into a custom binary format, using only as many bits as necessary to describe the submission, then converting it to Base64 so it uses printable characters and can be sent by SMS.

It is then intended that this library is used both on the mobile app side for compression and on the SMS gateway receiving side, for decompressing the received message, before sending it on to DHIS2.

### Limitations

Currently the compression library only supports DHIS2 aggregate data set reports.

## Using it

This compression library is built as a .jar file and intended to be included as a dependency in your `build.gradle`, for both app side and receiving side, i.e. the mobile app should be a native Android app and the receiving side will need to be a Java web server of some sort, which can decompress the incoming SMS before forwarding the message to DHIS2 in the expected format.

It uses a service called [Jitpack](https://jitpack.io) which automatically monitors public Github repositories and builds each new release into .jar files, which it makes publicly available. See [here](https://jitpack.io/#eHealthAfrica/dhis2-sms-comp) for a list of releases of this library as well as details about how to include it as a dependency.

### Compression

When you want to use the library app side to compress an SMS submission, you'll need to create an instance of the `SMSEncoder`. To do so, you'll need to pass the constructor an `ArrayList<SmsCommand>`. You can do so by using the `SmsCommandParser`, which takes a `String` which is the output from DHIS2's `/api/sms/commands` endpoint (or `/api/smsCommands` for newer versions).

The resulting call will look something like

```java
SmsEncoder encoder = new SmsEncoder(SmsCommandParser.parse(smsCmds));
```

After that, simply pass the typical DHIS2 SMS submission as a `String` to the `encode` method:

```java
byte[] encodedSMS = encoder.encode(plainTextSMS);
```

Then you'll want to Base64 encode the resulting `byte[]` so that it can be sent as plain text in an SMS.

### Decompression

Decompression is quite similar to Compression, you'll need to create an instance of the `SMSDecoder`, with the same `ArrayList<SmsCommand>`.

The `SMSDecoder.decode` method takes the `byte[]` that represents the compressed message and returns a `String` of the original plaintext DHIS2 SMS submission. As there have been major changes in the library, it will first attempt to decompress in the latest format, then if it fails, in the original format.

## Details of compression

The DHIS2 SMS submission looks something like the following:

```
smscommand 1910 gfjd:90|sfld:0|dasd:10
```

Where the first part is the name of the SMS command itself (which corresponds to a particular data set), then (optionally) the date for this submission (day then month), then come the key value pairs of the actual submission. Each key is a data element (catacombo) from the data set.

The compression library takes advantage of a number of principles in order to save space:

### Converting values to binary with adaptive bit lengths

When specifying values, we convert all numbers and dates (epoch time) to the binary equivalent, which saves quite a lot of space. The bit length of an integer is adaptive to the largest value integer found in the submission, which is then specified in the header of the compressed message. Similarly for the key lengths, the bit length is adaptive dependent on the number of keys for the command.

### Shared knowledge

Because both the compressor and decompressor receive the list of SMS commands on instantiation they are aware of all the keys for each command in the submission. Thus when specifying keys, instead of using the full key, we simply use the index of the key, sorted by creation date.

### Repeated values

Commonly with submissions there is one value which is repeated often, i.e. a '0'. The library takes advantage of this by simply specifying fields which are NOT this value and then simply saying "all other keys are: " `remainingValue`

