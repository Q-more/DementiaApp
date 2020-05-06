# Sažetak
Kod ranih stadija demencije osobe pate od iznenadnih napada panike i dezorijentiranosti. U takvim situacijama često je dovoljno izgovoriti njihovo ime, prezime ili
adresu na kojoj se nalaze da bi napad panike završio. Takod̄er vrlo je važno napomenuti da su osobe često dovoljno pribrane da mogu napraviti jednostavne radnje poput
pritiska gumba ili pogleda na kako bi se osvijestile i shvatile gdje se nalaze. Upravo zbog toga razvijeno je programsko rješenje koje pomaže u takvim situacijama, a sastoji se od
mobilne aplikacije i aplikacije za pametni sat. Mobilna aplikacija izvedena je kao alat
u kojem korisnik jednim pritiskom na gumb može dobiti informaciju tko je on i gdje
se trenutno nalazi. Takod̄er aplikacija se može podesiti da slanjem poruke obavjesti
obitelj o napadu i trenutnoj lokaciji oboljelog. Kao dodatna pomoć pri korištenju, mobilnu aplikaciju je moguće upariti s pametnim satom nakon čega se trenutnu lokaciju
može zatražiti i prikazati na pametnom satu. Prednost ove aplikacije se očituje u specifičnom dizajnu koji je prilagod̄en osobama koje prolaze kroz napade panike te ju to
čini vrijednim alatom za pomoć osobama u ranom stadiju demencije.

# DementiaApp aplikacija
Cijeli sustav zamišljen je kao asistivna tehnologija koja obuhvaća mobilni ured̄aj i pametni sat. Na ručnom satu moguće je pokrenuti aplikaciju koja se sastoji od jednog
gumba čijim pritiskom se korisniku predočuje trenutna lokacija. Mobila aplikacija takod̄er sadrži tu funkciju uz mogućnost zasebnog slanja poruka unutar same aplikacije.

## Mobilna aplikacija
Mobilnu aplikaciju DementiaApp moguće je instalirati na ured̄aje operacijskog sustava
Android verzije 7.0 i više koji imaju 3.7 MB slobodne memorije.

### Upute za korištenje
Prilikom prvog pokretanja aplikacije pojavljuje se forma za unos osobnih podataka 6.1. Korisnik mora unijeti ime, prezime i adresu te nakon toga pritisnuti gumb SPREMI
za nastavak. Ako podaci nisu uneseni, a pritisnut je gumb za nastavak, aplikacija
obavještava korisnika o pogrešci.

Slika 6.1: Sučelje za popunjavanje osobnih podataka          |  
:-------------------------:|
<img src="/resources/pocetna_unos.png" width="300" height="500" /> | 

Nakon unosa ispravnih podataka na ekranu se prikazuje veliki crveni gumb s natpisom LOCIRAJ ME, mali žuti gumb sa slikom omotnice te u gornjem lijevom kutu
ikona za ulaz u izbornik 6.3.
Pritiskom na gumb LOCIRAJ ME prikazuje se prethodno uneseno ime, prezime i
adresa 6.2. Uz osobne podatke prikazuje se trenutna lokacija korisnika. Ako aplikacija
nema prava za pristup lokaciji ili pristup lokaciji na ured̄aju nije dozvoljen aplikacija
obavještava korisnika o nemogućnosti prikaza lokacije. Osim prikaza podataka i lokacije pritiskom na gumb šalje se poruka o trenutnoj lokaciji svim kontaktima koje je
korisnik definirao kao primatelje.
Pritiskom na gumb s omotnicom korisniku se prikazuje sučelje u kojem ima mogućnost pisanja poruke na čiji se kraj dodaje trenutna lokacija ako je to moguće te se
zatim šalje kontaktima koje je korisnik definirao 6.4. Poruka neće biti poslana ako
aplikacija nema dozvolu slanja poruka ili ako niti jedan kontakt nije dodan na popis kontakata u slučaju napada panike.

6.2: Sučelje za prikaz lokacije             | 6.3: Glavno korisničko sučelje          | 6.4: Sučelje za slanje poruka                         |
:-------------------------:|:-------------------------:|:-------------------------:|
![dohvacena_lokacija](/resources/dohvacena_lokacija.png)  |  ![pocetna](/resources/pocetna.png) |  ![slanje_poruke_ekran](/resources/slanje_poruke_ekran.png)                         |

Pritiskom na gumb za izbornik ili prolaskom prsta s lijeva na desno po ekranu
ured̄aja otvara se glavni izbornik aplikacije. U gornjem lijevom kutu navedeni su ime,
prezime i adresa korisnika 6.5. Opcije koje je moguće odabrati su Osobni podaci,
Kontakti, Šalji, Postavke.

Slika 6.5: Sučelje izbornika            |  Slika 6.6: Sučelje promjena osobnih podataka         |  
:-------------------------:|:-------------------------:|
<img src="/resources/izbrnika.png" width="300" height="500" /> |<img src="/resources/promjena_podataka.png" width="300" height="500" />|  

U Osobnim podacima moguće je napraviti izmjenu podataka otvarajući prikaz koji
je bio prikazan prilikom prvog pokretanja aplikacije 6.6. Jedina razlika se očituje
u tome što se pritiskom na hardversku tipku "Back" ne izlazi iz aplikacije nego se
korisnik vraća u izbornik.
Ulaskom u Kontakti omogućuje se korisniku dodavanje kontakata kojima će biti
poslana poruka prilikom pritiska na gumb LOCIRAJ ME te gumb s omotnicom za
slanje poruke. U donjem desnom kutu nalazi se žuti gumb čijim se pritiskom otvara
imenik u kojem korisnik može odabrati kontakte koji će biti dodani na popis unutar
aplikacije 6.7.

Slika 6.7: Sučelje za prikaz dodanih kontakata            |  Slika 6.8: Brisanje kontakata          |  Slika 6.9: Vračanje izbrisanog kontakta u listu
:-------------------------:|:-------------------------:|:-------------------------:
![kontakti_item](/resources/kontakti_item.png) |  ![izbrisi](/resources/izbrisi.png) |  ![kontakti_undu](/resources/kontakti_undu.png)

Kontakti se mogu izbrisati s popisa prolaskom prsta po ekranu s lijeva prema desno
iznad odabranog kontakta 6.8. Nakon što je kontakt uklonjen s popisa na dnu ekrana,
crvenim slovima se prikazuje poruka, točnije riječ VRATI. Pritiskom na riječ VRATI
kontakt se vraća na popis kontakata 6.9. Opcija za slanje Šalji podupire jednaku funkcionalnost kao i pritisak na gumb s omotnicom koji je opisan ranije.
Pritiskom na Postavke otvaraju se postavke aplikacije 6.11. U postavkama korisnik
može zabraniti slanje poruka unutar aplikacije pritiskom na Toggle gumb. Ako je gumb
osjenčan žutom bojom poruke su omogućene 6.10. Prilikom odabira opcije Sadržaj
iskače prozor u kojem se može definirati sadržaj poruke koja se šalje pritiskom na
gumb LOCIRAJ ME 6.12. Sadržaj poruke se sprema pritiskom na gumb OK ili se
promjena zanemaruju ako se pritisne gumb CANCEL ili ako se pritisne izvan samog iskočnog prozora.

Slika 6.10: Slanje poruka omogućeno          |  Slika 6.11: Slanje poruka onemogućeno          |  Slika 6.12: Promjena sadržaja poruke
:-------------------------:|:-------------------------:|:-------------------------:
![postavke](/resources/postavke.png)  |  ![postavke_sadrzaj](/resources/postavke_sadrzaj.png) | ![postavke_sadrzaj](/resources/postavke_sadrzaj.png)


### Dozvole

Prilikom prvog pokretanja DementiaApp aplikacije prikazuje se sučelje korisniku u
kojem se može dozvoliti aplikaciji pristup lokaciji 6.13, kontaktima 6.14 i slanju poruka 6.15. Ako korisnik nije dozvolio pristup lokaciji, prikaz lokacije unutat aplikacije
neće biti mogućl. U slučaju da korisnik nije dozvolio pristup kontaktima, neće biti
moguće dodati kontakte za slanje poruka. Ako korisnik nije dozvolio slanje poruka iz
aplikacije, slanje poruka neće biti omogućeno. Korisnik u bilo kojem trenutku može
omogućiti ili ne omogućiti bilo koju od navedenih dozvola u postavkama telefona.

Slika 6.13: Sučelje za dozvolu pristupa lokaciji        |  Slika 6.14: Sučelje za dozvolu pristupa kontaktima          | Slika 6.15: Sučelje za dozvolu slanja poruka
:-------------------------:|:-------------------------:|:-------------------------:
![dozvola_lokacija](/resources/dozvola_lokacija.png)  |  ![dozvola_kontakti](/resources/dozvola_kontakti.png) | ![dozvola_poruke](/resources/dozvola_poruke.png)

## Aplikacija za pametni sat
Wear aplikacija za pametni sat zahtjeva minimalnu verziju Android 7.1.1 operacijskog
sustava i 2.2 MB slobodne memorije. Da bi korištenje aplikacije bilo moguće potrebno
je upariti pametni sat s mobilnim ured̄ajem.

### Upute za korištenje
Prije korištenja aplikacije potrebno je preuzeti i instalirati DementiaApp aplikaciju na
mobilni ured̄aj, kako je opisano ranije. Da bi korištenje aplikacije na pametnom satu
bilo moguće pokrenite istoimenu aplikaciju i na pametnom telefonu te zatim provje-
rite jesu li ured̄aji upareni. Ako ured̄aji nisu upareni, uparite ih. Prilikom pokretanja
aplikacije na pametnom satu na ekranu se pojavljuje crveni gumb s natpisom Lociraj
me 6.16 . Pritiskom gumba na ekranu se prikazuje trenutna adresa korisnika 6.17. Za
povratak na početni ekran potrebno je povući prstom po ekranu s desna na lijevo.

Slika 6.16: Gumb za dohvat lokacije           |  Slika 6.17: Sučelje za prikaz lokacije         |  
:-------------------------:|:-------------------------:|
![sat_gumb](/resources/sat_gumb.png) |![sat_tekst](/resources/sat_tekst.png)|  

