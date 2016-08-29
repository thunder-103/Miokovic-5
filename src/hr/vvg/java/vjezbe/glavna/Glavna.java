package hr.vvg.java.vjezbe.glavna;

import hr.vvg.java.vjezbe.entitet.Casopis;
import hr.vvg.java.vjezbe.entitet.Clan;
import hr.vvg.java.vjezbe.entitet.Izdavac;
import hr.vvg.java.vjezbe.entitet.Knjiga;
import hr.vvg.java.vjezbe.entitet.Knjiznica;
import hr.vvg.java.vjezbe.entitet.Posudba;
import hr.vvg.java.vjezbe.entitet.Publikacija;
import hr.vvg.java.vjezbe.enumeracija.Jezik;
import hr.vvg.java.vjezbe.enumeracija.VrstaPublikacije;
import hr.vvg.java.vjezbe.iznimke.DuplikatPublikacijeException;
import hr.vvg.java.vjezbe.iznimke.NeisplativoObjavljivanjeException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;



public class Glavna {
	
	private static final Logger log = LoggerFactory.getLogger(Glavna.class);
	
	public static void main(String[] args) {
		
		 Scanner ulazni = new Scanner(System.in);
		 
		 //Publikacija publikacija[] = new Publikacija[4];
		 
		 Knjiznica<Publikacija> knjiznica = new Knjiznica<>();
		 
		 //List<Publikacija> listaPublikacija = new ArrayList<>(); 
		
		 Clan clan;
		
		 Posudba<Publikacija> posudba = null;
		 Boolean dobraKnjiga = false;
		 Boolean dobarCasopis = false;
		 
		
		for (int i = 0; i < 2; i++){
			int k = 1;
			do {
				
				try {
					
					log.info("Unos " + k + ". publikacije");
					Publikacija publikacija = unesiKnjigu(knjiznica, i, ulazni);
					knjiznica.dodajPublikaciju(publikacija);
					k++;
					dobraKnjiga = true;
				
				}
				catch (NeisplativoObjavljivanjeException ek) {
					
					System.out.println(ek.getMessage());
					log.error("Nije isplativo za objavu", ek);
					dobraKnjiga = false;
				}
				catch (DuplikatPublikacijeException exk) {
					
					System.out.println(exk.getMessage());
					log.error("Dupla publikacija!", exk);
					dobraKnjiga = false;
				}
			
			}while(dobraKnjiga == false);
		} 
		
		for (int i = 2; i < 4; i++){
			int j = 1;
			do {
				
				try {
					
					log.info("Unos " + j + ". publikacije");
		
					Publikacija publikacija = unesiCasopis(knjiznica, i, ulazni);
					knjiznica.dodajPublikaciju(publikacija);
					dobarCasopis = true;
				
				}
				catch (NeisplativoObjavljivanjeException ec) {
					
					System.out.println(ec.getMessage());
					log.error("Nije isplativo za objavu", ec);
					dobarCasopis = false;
				}
				catch (DuplikatPublikacijeException exc) {
					
					System.out.println(exc.getMessage());
					log.error("Dupla publikacija!", exc);
					dobarCasopis = false;
				}
			
			}while(dobarCasopis == false);
		}
		
		najskupljaPublikacija(knjiznica);
		
		najjeftinijaPublikacija(knjiznica);
		
		
		clan = unosClana(ulazni);
		log.info("Unos clana", clan);
		
		do {
		
		posudba = odaberiPublikaciju(knjiznica, clan, ulazni);
		log.info("Odabrana publikacija: ", posudba.getPublikacija().getNaziv());
		
		} while (posudba == null);
		
		stanjePosudbe(posudba);
		
		pretragaPublikacije(knjiznica, ulazni);
		
		ulazni.close();	
		
	}
	
	private static Knjiga unesiKnjigu(Knjiznica<Publikacija> knjiznica ,int kolikoKnjiga, 
			Scanner ulazni) throws DuplikatPublikacijeException {
		
			BigDecimal cijenaPoStranici;
			boolean dobarJezik = false;
			
			System.out.println("Unos " + (kolikoKnjiga + 1) + ". knjige:");
			
			System.out.println("Molimo, unesite naslov knjige >>");
			String naslovKnjige = ulazni.nextLine();
			
			
			System.out.println("Molimo, unesite jezik >>");
			
			for(Jezik jezik : Jezik.values()) {
				
				System.out.println(jezik.getBrojJezika() + " " + jezik.getNazivJezika());

			}
			
			Jezik jezikKnjige = null;
			
			int brojIzNumeracije = 0;
			
			do{
			
			
				int brojJezikaKnjige = ulazni.nextInt();
				
				for(Jezik jezik : Jezik.values()) {
					
					brojIzNumeracije = jezik.getBrojJezika();
					
					if (brojJezikaKnjige == brojIzNumeracije) {
						
						jezikKnjige = Jezik.values()[brojJezikaKnjige - 1];
						
						dobarJezik = true;
						break;
					}
					
				}
				
				if (dobarJezik == false) {
					
					System.out.println("Unesite ponovo jezik knjige:");
					
				}
							
			} while (dobarJezik == false);
			
			
	
			System.out.println("Molimo, unesite godinu izdanja >>");
			int godinaIzdanja = ulazni.nextInt();
			
			System.out.println("Molimo, unesite broj stranica knjige >>");
			int brojStranica = ulazni.nextInt();
			
			VrstaPublikacije vrstaPublikacijeKorisnik = null;
			int brojVrstePublikacije = 0;
			boolean dobraPublikacija = false;
			
			System.out.println("Molimo, unesite vrstu publikacije >>");
			
			for(VrstaPublikacije vrstaPublikacije : VrstaPublikacije.values()) {
				
				System.out.println(vrstaPublikacije.getBrojVrste() + " " 
				+ vrstaPublikacije.getNazivVrste());

			}
			
			do{
			
			
				int brojUnosaKorisnika = ulazni.nextInt();
				ulazni.nextLine();
				
				for(VrstaPublikacije vrstaPublikacije : VrstaPublikacije.values()) {
					
					brojVrstePublikacije = vrstaPublikacije.getBrojVrste();
					
					if (brojUnosaKorisnika == brojVrstePublikacije) {
						
						vrstaPublikacijeKorisnik = VrstaPublikacije.values()[brojUnosaKorisnika - 1];
						
						dobraPublikacija = true;
						break;
					}
					
				}
				
				if (dobraPublikacija == false) {
					
					System.out.println("Unesite ponovo jezik knjige:");
					
				}
							
			} while (dobraPublikacija == false);
			
			
			
			System.out.println("Molimo, unesite izdavaèa >>");
			String izdavacKnjige = ulazni.nextLine();
			
			System.out.println("Molimo, unesite državu  >>");
			String drzavaKnjige = ulazni.nextLine();
			
			Izdavac izdavac = new Izdavac(izdavacKnjige, drzavaKnjige);
			
			if (izdavac.getDrzavaIzdavaca().equals("Hrvatska")) {
				
				cijenaPoStranici = Knjiga.getCijenaPoStraniciHr();
			}
			else {
				
				cijenaPoStranici = Knjiga.getCijenaPoStraniciForeign();
			}
			
			Boolean dostupnaZaPosudbu = true;
			
			
			Knjiga knjiga = new Knjiga(naslovKnjige, jezikKnjige, izdavac, godinaIzdanja,
					vrstaPublikacijeKorisnik, cijenaPoStranici, brojStranica, dostupnaZaPosudbu);
	
			for (int j = 0; j < knjiznica.dohvatiSvePublikacije().size(); j++) {
				
				if (knjiga.equals(knjiznica.dohvatiSvePublikacije().get(j))) {
						
					throw new DuplikatPublikacijeException("Pogreška! Unijeli ste duplikat knjige,"
							+ " molimo ponovite upis!");
				}
			}
			
			return knjiga;
	}
	
	private static Clan unosClana(Scanner ulazni) {
		
		System.out.println("Unos èlana:");
		
		System.out.println("Molimo, unesite OIB èlana >>");
		String oibClana = ulazni.nextLine();
		
		System.out.println("Molimo, unesite ime èlana >>");
		String imeClana = ulazni.nextLine();
		
		System.out.println("Molimo, unesite prezime èlana >>");
		String prezimeClana = ulazni.nextLine();
		
		return new Clan(imeClana, prezimeClana, oibClana);
	}
	
	private static Casopis unesiCasopis(Knjiznica<Publikacija> knjiznica ,int kolikoCasopisa, 
			Scanner ulazni) throws DuplikatPublikacijeException {
		
		System.out.println("Unos " + (kolikoCasopisa - 1) + ". casopisa:");
		
		System.out.println("Molimo, unesite naslov casopisa >>");
		String naslovCasopisa = ulazni.nextLine();

		System.out.println("Molimo, unesite godinu izdanja casopisa >>");
		int godinaIzdanjaCasopisa = ulazni.nextInt();
		
		System.out.println("Molimo, unesite broj stranica casopisa >>");
		int brojStranicaCasopisa = ulazni.nextInt();
		
		System.out.println("Molimo, unesite mjesec izdanja casopisa >>");
		int mjesecIzdanjaCasopisa = ulazni.nextInt();
		
		VrstaPublikacije vrstaPublikacijeKorisnik = null;
		int brojVrstePublikacije = 0;
		boolean dobraPublikacija = false;
		
		System.out.println("Molimo, unesite vrstu publikacije >>");
		
		for(VrstaPublikacije vrstaPublikacije : VrstaPublikacije.values()) {
			
			System.out.println(vrstaPublikacije.getBrojVrste() + " " 
			+ vrstaPublikacije.getNazivVrste());

		}
		
		do{
		
		
			int brojUnosaKorisnika = ulazni.nextInt();
			ulazni.nextLine();
			
			for(VrstaPublikacije vrstaPublikacije : VrstaPublikacije.values()) {
				
				brojVrstePublikacije = vrstaPublikacije.getBrojVrste();
				
				if (brojUnosaKorisnika == brojVrstePublikacije) {
					
					vrstaPublikacijeKorisnik = VrstaPublikacije.values()[brojUnosaKorisnika - 1];
					
					dobraPublikacija = true;
					break;
				}
				
			}
			
			if (dobraPublikacija == false) {
				
				System.out.println("Unesite ponovo jezik knjige:");
				
			}
						
		} while (dobraPublikacija == false);
		
		Casopis casopis = new Casopis(naslovCasopisa, godinaIzdanjaCasopisa, brojStranicaCasopisa, 
				vrstaPublikacijeKorisnik, mjesecIzdanjaCasopisa);
		
		for (int j = 0; j < knjiznica.dohvatiSvePublikacije().size(); j++) {
			
			if (casopis.equals(knjiznica.dohvatiSvePublikacije().get(j))) {
					
				throw new DuplikatPublikacijeException("Pogreška! Unijeli ste duplikat casopisa,"
						+ " molimo ponovite upis!");
			}
		}
		
		return casopis;
	}
	
	private static Posudba<Publikacija> odaberiPublikaciju(Knjiznica<Publikacija> knjiznica, Clan clan, 
			Scanner ulazni) {
		
		System.out.println("Molimo, odaberite publikaciju:");
		
		String nazivPublikacije = ulazni.nextLine();
		
		//List<Publikacija> listaPublikacija = new ArrayList<>();
		
		Publikacija publikacija = knjiznica.dohvatiSvePublikacije().stream()
				.filter(p -> p.getNaziv().equals(nazivPublikacije)).findFirst().get();
		
//			for (int i = 0; i < listaPublikacija.size(); i++) {
//		
//				System.out.println((i+1) + ")" + listaPublikacija.get(i).getNaziv());
//				
//			}
//			
			LocalDateTime datumPosudbe = LocalDateTime.now();
			
			
			return new Posudba<Publikacija>(clan, publikacija, datumPosudbe);
	}

	
	private static void stanjePosudbe(Posudba<Publikacija> posudba) {
		
		System.out.println("Stanje posudbe:");
		
		System.out.println("Naziv publikacije: " + posudba.getPublikacija().getNaziv());
		
		System.out.println("Vrsta publikacije: " + posudba.getPublikacija().getVrstaPublikacije());
		
		System.out.println("Broj stranica: " + posudba.getPublikacija().getBrojStranica());
		
		System.out.println("Cijena: " + posudba.getPublikacija().getCijena());
		
		Publikacija publikacija = posudba.getPublikacija();
		
		if (publikacija instanceof Knjiga){
			
			Knjiga knjiga = (Knjiga) publikacija;
		
			System.out.println("Jezik: " + knjiga.getJezikKnjige());
			
			System.out.println("Izdavaè: " + knjiga.getIzdavacKnjige().getNazivIzdavaca());
			
			System.out.println("Država izdavaèa: " + knjiga.getIzdavacKnjige().getDrzavaIzdavaca());
			
			boolean raspolozivost = knjiga.provjeriRaspolozivost();
			String raspolozivostKaoString = new Boolean(raspolozivost).toString();
			
			raspolozivostKaoString = raspolozivostKaoString.replaceAll("false", "NE");
			raspolozivostKaoString = raspolozivostKaoString.replaceAll("true", "DA");
			
			System.out.println("Raspoloživo za posudbu: " + raspolozivostKaoString);
		}
		else {
			
			Casopis casopis = (Casopis) publikacija;
			
			System.out.println("Mjesec izdanja: " + casopis.getMjesecIzdavanjaCasopisa());
		}
		
		System.out.println("Podaci korisnika: ");
		
		System.out.println("Prezime: " + posudba.getNekiClan().getPrezimeClana());
		
		System.out.println("Ime: " + posudba.getNekiClan().getImeClana());
		
		System.out.println("OIB: " + posudba.getNekiClan().getOibClana());
		
		DateTimeFormatter formaterDatuma = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		
		System.out.println("Datum posudbe: " + 
							posudba.getDatumPosudbe().format(formaterDatuma));
			
	}
	
	private static void najjeftinijaPublikacija(Knjiznica<Publikacija> knjiznica) {
		
		Optional<Publikacija> najjeftinijaPublikacija = 
				knjiznica.dohvatiSvePublikacije().stream()
				.sorted((p1, p2) -> p1.getCijena().compareTo(p2.getCijena())).findFirst();

		//System.out.println(najjeftinijaPublikacija.get());
		
		System.out.println("Najjeftinija publikacija:");
		
		System.out.println("Naziv publikacije: " + najjeftinijaPublikacija.get().getNaziv());
		
		System.out.println("Vrsta publikacije: " + najjeftinijaPublikacija.get().getVrstaPublikacije());
		
		System.out.println("Broj stranica: " + najjeftinijaPublikacija.get().getBrojStranica());
		
		System.out.println("Cijena: " + najjeftinijaPublikacija.get().getCijena());
		
		if (najjeftinijaPublikacija.get() instanceof Knjiga){
			
			Knjiga knjiga = (Knjiga) najjeftinijaPublikacija.get();
		
			System.out.println("Jezik: " + knjiga.getJezikKnjige());
			
			System.out.println("Izdavaè: " + knjiga.getIzdavacKnjige().getNazivIzdavaca());
			
			System.out.println("Država izdavaèa: " + knjiga.getIzdavacKnjige().getDrzavaIzdavaca());
			
			boolean raspolozivost = knjiga.provjeriRaspolozivost();
			String raspolozivostKaoString = new Boolean(raspolozivost).toString();
			
			raspolozivostKaoString = raspolozivostKaoString.replaceAll("false", "NE");
			raspolozivostKaoString = raspolozivostKaoString.replaceAll("true", "DA");
			
			System.out.println("Raspoloživo za posudbu: " + raspolozivostKaoString);
		}
		
		else {
			
			Casopis casopis = (Casopis) najjeftinijaPublikacija.get();
			
			System.out.println("Mjesec izdanja: " + casopis.getMjesecIzdavanjaCasopisa());
		}
	}
	
	private static void najskupljaPublikacija(Knjiznica<Publikacija> knjiznica) {
		
		Optional<Publikacija> najskupljaPublikacija = 
				knjiznica.dohvatiSvePublikacije().stream()
				.sorted((p1, p2) -> p1.getCijena().compareTo(p2.getCijena())).findFirst();

		//System.out.println(najskupljaPublikacija.get());
		
		System.out.println("Najskuplja publikacija:");
		
		System.out.println("Naziv publikacije: " + najskupljaPublikacija.get().getNaziv());
		
		System.out.println("Vrsta publikacije: " + najskupljaPublikacija.get().getVrstaPublikacije());
		
		System.out.println("Broj stranica: " + najskupljaPublikacija.get().getBrojStranica());
		
		System.out.println("Cijena: " + najskupljaPublikacija.get().getCijena());
		
		if (najskupljaPublikacija.get() instanceof Knjiga){
			
			Knjiga knjiga = (Knjiga) najskupljaPublikacija.get();
		
			System.out.println("Jezik: " + knjiga.getJezikKnjige());
			
			System.out.println("Izdavaè: " + knjiga.getIzdavacKnjige().getNazivIzdavaca());
			
			System.out.println("Država izdavaèa: " + knjiga.getIzdavacKnjige().getDrzavaIzdavaca());
			
			boolean raspolozivost = knjiga.provjeriRaspolozivost();
			String raspolozivostKaoString = new Boolean(raspolozivost).toString();
			
			raspolozivostKaoString = raspolozivostKaoString.replaceAll("false", "NE");
			raspolozivostKaoString = raspolozivostKaoString.replaceAll("true", "DA");
			
			System.out.println("Raspoloživo za posudbu: " + raspolozivostKaoString);
		}
		
		else {
			
			Casopis casopis = (Casopis) najskupljaPublikacija.get();
			
			System.out.println("Mjesec izdanja: " + casopis.getMjesecIzdavanjaCasopisa());
		}
	}
	
	private static void pretragaPublikacije(Knjiznica<Publikacija> knjiznica , Scanner ulazni) {
		
		System.out.println("Pretražite publikacije:");
		
		String rijecPretrage = ulazni.nextLine();
		
		List<Publikacija> publikacija = knjiznica.dohvatiSvePublikacije().stream().filter(p -> p.getNaziv()
				.contains(rijecPretrage)).collect(Collectors.toList());
		
		for (Publikacija trenutnaPublikacija: publikacija) {
		
		System.out.println("Naziv publikacije: " + trenutnaPublikacija.getNaziv());
		
		System.out.println("Vrsta publikacije: " + trenutnaPublikacija.getVrstaPublikacije());
		
		System.out.println("Broj stranica: " + trenutnaPublikacija.getBrojStranica());
		
		System.out.println("Cijena: " + trenutnaPublikacija.getCijena());
		
		if (trenutnaPublikacija instanceof Knjiga){
			
			Knjiga knjiga = (Knjiga) trenutnaPublikacija;
		
			System.out.println("Jezik: " + knjiga.getJezikKnjige());
			
			System.out.println("Izdavaè: " + knjiga.getIzdavacKnjige().getNazivIzdavaca());
			
			System.out.println("Država izdavaèa: " + knjiga.getIzdavacKnjige().getDrzavaIzdavaca());
			
			boolean raspolozivost = knjiga.provjeriRaspolozivost();
			String raspolozivostKaoString = new Boolean(raspolozivost).toString();
			
			raspolozivostKaoString = raspolozivostKaoString.replaceAll("false", "NE");
			raspolozivostKaoString = raspolozivostKaoString.replaceAll("true", "DA");
			
			System.out.println("Raspoloživo za posudbu: " + raspolozivostKaoString);
		}
		else {
			
			Casopis casopis = (Casopis) trenutnaPublikacija;
			
			System.out.println("Mjesec izdanja: " + casopis.getMjesecIzdavanjaCasopisa());
		}
		
		}
		
	}
	
}
