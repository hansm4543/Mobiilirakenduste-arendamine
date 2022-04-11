# Harjutus 6

Harjutusel osalesid
- Markus Veem 
- Markus Tammeoja
- Hans-Märten Liiu
- Anastasija Selevjorstova
- Jan-Erik Läänesaar

Alammoodulis `android` asub rakendus, mis on osa meie projekti visioonist.
Ta sisaldab:
- Piltide üleslaadimine telefoni galeriist
- Piltide üleslaadimine kaamerast
- Ajaloo hoidmine .json failina
- Pildi copy/paste
- Ajaloo kustutamine

### Mis oli raske
- JSON serialisatsioon (ja veel nt Gson vs Mochi vs Jackson vs kotlinx.serialization dilemma)
- Piltide majandamine (ja töötlemine - nt URI konverteerimine Bitmapi)
- Pidi Fileprovider'iga majandama, et saada pildi URI kätte
- Liiga vähe dokumentatsiooni/õpetusi
- Imgur blokeeris ära Eesti telefoninumrid, seega ei saanud koheselt konto teha API kasutuseks.
- Tuli välja, et assets kaust on read-only ja on pakitud APK sisse
- Coroutines on vahest endiselt väga segased
- Recyclerview sättimine ja nendesse eventlisteneri bindimine oli alguses raske
- Networking on väga tüütu ja pikk (et teha POST imgurile näiteks)

### Mis oli kerge
- ABSOLUUTSELT MITTE MIDAGI