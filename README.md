# Android-Load_Certificate_Digital

```
 File file = new File(path);
            KeyStore p12 = KeyStore.getInstance("pkcs12");
            p12.load(new FileInputStream(file), editPassword.getText().toString().toCharArray());
            Enumeration e = p12.aliases();
            while (e.hasMoreElements()) {
                String alias = (String) e.nextElement();
                X509Certificate c = (X509Certificate) p12.getCertificate(alias);
                Principal subject = c.getSubjectDN();
                String subjectArray[] = subject.toString().split(",");
                for (String s : subjectArray) {
                    String[] str = s.trim().split("=");
                    String key = str[0];
                    String value = str[1];
                    System.out.println(key + " - " + value);
                    System.out.println("key "+key);
                    Log.e("data",value);
                    txtResult.setText(txtResult.getText()+"\n"+key+" "+value);
                }
            }
```
![alt tag](https://aeroyid.files.wordpress.com/2017/01/photo_2017-01-18_13-32-49.jpg)
