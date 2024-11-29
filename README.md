# archer-tools
Useful tools written with pure java. Includes ecdsa, sm2, sm3, encryption, excel reader, http(s) client and server etc.

## Asm java bytecode
see [AsyncProxy.java](https://github.com/Archerxy/archer-framework/blob/main/src/main/java/com/archer/framework/base/async/AsyncProxy.java) and [TestService.test1](https://github.com/Archerxy/archer-framework/blob/main/src/demos/com/archer/test/run/TestService.java) call Async test3()   
the example shows how to use 'com.archer.tools.bytecode.ClassBytecode' for generating a child class and change the behavier of the super class  

## Digital signatures  
``` java  
    byte[] sk = {29, -3, 74, 47, 123, 64, 41, 123, 67, -9, 89, 16, 84, 115, 18, -8, -41, -97, -57, 36, 103, 60, 115, -123, -5, -38, -97, 127, 32, -21, -25, 2};  
    byte[] pk = {36, 117, -87, 86, -21, 0, 78, 37, -128, -38, -1, -36, -74, -16, 60, -55, -46, 47, -29, -101, 95, 53, 113, 31, 0, 37, -46, 89, -70, -126, 10, -86, 44, -69, -127, -11, -19, 120, -83, 90, 46, 81, 15, -101, -16, -87, -106, -67, -33, -23, 18, 54, -67, 36, 99, 11, 59, -73, -96, 99, -98, 95, -115, -68};  
    byte[] hash = {21, -31, 75, 47, 123, 64, 41, 123, 67, -9, 89, 16, 84, 115, 18, -8, -41, -97, -57, 36, 103, 60, 115, -123, -5, -38, -97, 127, 32, -21, -25, 2};  
    Signature sig = new Signature(Signature.PopularCurve.secp256k1);  
  
    byte[] rs = sig.sign(sk, hash);  
    // print result  
    System.out.println("ret: "+sig.verify(pk, hash, rs));  
    
    byte[] sm2Sk = {29, -3, 74, 47, 123, 64, 41, 123, 67, -9, 89, 16, 84, 115, 18, -8, -41, -97, -57, 36, 103, 60, 115, -123, -5, -38, -97, 127, 32, -21, -25, 2};
    byte[] sm2Pk = {124, -111, 78, 61, -127, 10, -126, -115, 18, -118, 16, 64, 63, -12, 77, 32, 8, 95, -32, 73, 36, 98, 63, -81, -1, -112, -45, -87, -119, -31, -91, -5, -76, 120, -20, -101, -57, 45, -115, -110, -52, -50, 83, -74, -117, -113, -38, -51, -125, 18, -42, -84, 59, -33, -105, -3, 23, -8, 83, 51, 45, 74, -31, -105};
    Signature sig = new Signature(Signature.PopularCurve.sm2p256v1);

    rs = sig.sign(sm2Sk, hash);
    // print result
    System.out.println("ret: "+sig.verify(sm2Pk, hash, rs));
```   

## crypto
``` java  
    SM2KeyPair keypair = SM2Crypto.genSM2KeyPair();
    byte[] pri = keypair.getPrivateKey();
    byte[] pub = keypair.getPublicKey(); //SM2Crypto.getPublicKeyFromPrivateKey(pri);
    	
    	
    byte[] data = "nihao, shuaige".getBytes();
    byte[] cipher = SM2Crypto.encrypt(pub, data, SM2CryptoMode.C1C2C3);
    // print cipher
    System.out.println(Arrays.toString(cipher));
    byte[] text = SM2Crypto.decrypt(pri, cipher, SM2CryptoMode.C1C2C3);
    // print text
    System.out.println(new String(text));
```  

## http(s) client
``` java   
    String body = JSONRequest.get("https://www.baidu.com");
    // print body
    System.out.println(body);

    BaseRequest<UserLoginReqVO> userLoginReq = ...;
    BaseResonse<UserLoginRspVO> userLoginRsp = JSONRequest.post("http://localhost:8080/api/test/login", userLoginReq, new JavaTypeRef<BaseResonse<UserLoginRspVO>>() {});
    // print body
    System.out.println(userLoginRsp);
```  

## http(s) server
``` java  
    (new SimpleHttpServer()).listen(8080, new HttpListener() {
        @Override
        public void inComingMessage(HttpRequest req, HttpResponse res) {
            // print request body
            System.out.println("req: " + new String(req.getContent()));
            String body = "{\"nihao\":\"shuaige\"}";
            res.setContentType(ContentType.APPLICATION_JSON);
            res.setContent(body.getBytes());
            }

        @Override
        public void onServerException(HttpRequest req, HttpResponse res, Throwable t) {
            t.printStackTrace();
        }
    });
```  

## read a xlsx file
``` java  
    // reading a middle size file
    List<Sheet> sheets = XlsxReader.read("e:/work/10w_line.xlsx");
    for(Sheet s: sheets) {
        System.out.println(s.getName());
        for(Row r: s.rows()) {
            for(Cell c: r.cells()) {
                System.out.println(c.getName() + ":" + c.getValue());
            }
        }
    }

    // reading a large size file
    List<SimpleSheet> simpleSheets = SimpleXlsxReader.read("e:/work/100w_line.xlsx");
    for(SimpleSheet s: simpleSheets) {
        System.out.println(s.getName());
        for(List<String> row: s.rows()) {
            for(String cell: row) {
                System.out.println(cell);
            }
        }
    }
```  

## sort algorithm
``` java  
    int count = 100000000;
    Integer[] a = new Integer[count];
    Random r = new Random();
    for(int i = 0; i < count; i++) {
        a[i] = r.nextInt(count);
    }
	
    System.out.println(Arrays.toString(Arrays.copyOfRange(a, 0, 20)));
    System.out.println(Arrays.toString(Arrays.copyOfRange(a, count -20, count)));
    QuickSort.sort(a, new Comparison<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
    }});

    System.out.println(Arrays.toString(Arrays.copyOfRange(a, 0, 20)));
    System.out.println(Arrays.toString(Arrays.copyOfRange(a, count -20, count)));
```  

