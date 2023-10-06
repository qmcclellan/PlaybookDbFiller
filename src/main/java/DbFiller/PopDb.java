package DbFiller;

import DbFiller.Dao.*;
import DbFiller.Entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PopDb {

    private static Map<String, Team> teamsMap;

    private static Map<String, PlayBook> playBookMap;
   // private static Map<String, ArrayList<Formation>> formationMap;

    private static Map<String, Formation> formationMap;
    private static Map<String, Scheme> schemeMap;
    private static Map<String, ArrayList<Play>> playMap;
    private final String teamImages = "https://loodibee.com/nfl/";
    private final String teamPlaybooks = "https://huddle.gg/23/playbooks/";
    private final ChromeOptions options = new ChromeOptions();
    private List<WebElement> playBookLinks;
    private SessionFactory factory;

    public PopDb() {
    }


    public static void main(String[] args) throws InterruptedException {


        PopDb popDb = new PopDb();

       // popDb.getTeamImages(); //team image site

      //  popDb.getPlayBooks(popDb.teamPlaybooks, "offense");

      //  popDb.getPlayBooks(popDb.teamPlaybooks, "defense");

     //  popDb.loadFormations("offense");

      //  popDb.loadSchemes("offense");

     //   popDb.loadPlays("defense");

      //  popDb.loadFormations("defense");

       //popDb.loadSchemes("defense");

        popDb.loadPlays("defense");

     //   popDb.loadPlays("defense");

    }

    public void getTeamImages() {


        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Team.class)
                .addAnnotatedClass(PlayBook.class)
                .addAnnotatedClass(Coach.class)
                .addAnnotatedClass(Formation.class)
                .addAnnotatedClass(Play.class)
                .addAnnotatedClass(Scheme.class)
                .buildSessionFactory();

        teamsMap = new HashMap<>();

        WebDriver driver = createDriver();

        driver.get(teamImages);

        String teamName;

        String imagePath;

        List<WebElement> elements;

        TeamDao teamDao = new TeamDao();


        for (int i = 0; i <= driver.findElement(By.cssSelector("[itemprop] [class='logos-layout column-3']:nth-child(10)")).findElements(By.cssSelector("figure")).size() - 1; i++) {

            //   System.out.println(i);

            //Get team name
            teamName = driver.findElement(By.cssSelector("div:nth-of-type(5) > figure:nth-of-type(" + (i + 1) + ") > figcaption > a")).getText();

            // System.out.println(i+teamName);
            //Click on team
            driver.findElement(By.xpath("//main[@id='main']/article//div[@class='entry-content']/div[5]/figure[" + (i + 1) + "]")).click();

            //Get team image list
            elements = driver.findElement(By.xpath("//main[@id='main']/article//div[@class='entry-content']/div[3]")).findElements(By.cssSelector("figure"));

            //  System.out.println(i + elements.get(1).findElement(By.cssSelector("img")).getAttribute("data-src"));

            if (driver.findElement(By.xpath("//main[@id='main']/article//div[@class='entry-content']/div[3]")).findElements(By.cssSelector("figure")).size() == 0) {

                //Get team image list
                elements = driver.findElement(By.xpath("//main[@id='main']/article//div[@class='entry-content']/div[4]")).findElements(By.cssSelector("figure"));

                //Get image location
                imagePath = elements.get(1).findElement(By.cssSelector("img")).getAttribute("src");

                imagePath = saveImage(imagePath, teamName, i);

                Team team = new Team(teamName, imagePath);

                //  System.out.println(team);
                //   teamInfo.add(team);
                driver.navigate().back();

                //Remove to save to db
                teamDao.save(factory, team);


                teamsMap.put(teamName, team);
                continue;
            }
            //Get image location
            imagePath = elements.get(1).findElement(By.cssSelector("img")).getAttribute("data-src");

            imagePath = saveImage(imagePath, teamName, i);

            Team team = new Team(teamName, imagePath);


            //Keep to save to db

            teamDao.save(factory, team);

            teamsMap.put(teamName, team);

            driver.navigate().back();
        }

        //Remove to save to db
        // teamDao.saveAll(factory, teamInfo);

        factory.close();
        driver.close();

        driver.quit();

        // System.out.println(teams);

    }

    public Map<String, PlayBook> getPlayBooks(String site, String playBookType) {


        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Team.class)
                .addAnnotatedClass(PlayBook.class)
                .addAnnotatedClass(Coach.class)
                .addAnnotatedClass(Formation.class)
                .addAnnotatedClass(Play.class)
                .addAnnotatedClass(Scheme.class)
                .buildSessionFactory();

        int formationTotal = 0;
        String playbookName;
        String formationName;
        List<WebElement> playBookLinks = null;
        TeamDao teamDao = new TeamDao();
        PlayBookDao playBookDao = new PlayBookDao();
        FormationDao formationDao = new FormationDao();

        playBookMap = new HashMap<>();

        teamsMap = new HashMap<>();

        WebDriver driver = createDriver();

        List<Team> teams = teamDao.findAll(factory);

        for(Team team : teams){

            teamsMap.put(team.getName(), team);
        }

        driver.get(site);

        int offTotal = 32;

        for (int i = 0; i <= offTotal - 1; i++) {

            //  System.out.println(i);

            //Get list of all playbook links
            playBookLinks = choosePlayBooks(driver, playBookType);

            Set<Formation> formations = new HashSet<>();

            String teamName = playBookLinks.get(i).getText();

            System.out.println(teamName);

            playBookLinks.get(i).click();

            try {

                //Get Playbook name
                playbookName = driver.findElement(By.cssSelector(".heading.heading--blue")).getText();

                Team team =  teamMapSearch(teamName);

                PlayBook book = new PlayBook(teamName, playBookType, null, team);

                playBookDao.save(factory, book);

                playBookMap.put(playbookName, book);

                    System.out.println(book);

                //Getting the number of formations on page
                formationTotal = driver.findElement(By.xpath("/html//div[@class='body-content']/div//div[@class='l-sidebar__main']/div")).findElements(By.cssSelector("h3")).size();

//                       System.out.println(formationTotal);

                for (int f = 1; f <= formationTotal; f++) {
                    //Getting formation name
                    formationName = driver.findElement(By.cssSelector(".py-5.wrapper > h3:nth-of-type(" + f + ")")).getText();

                    Formation formation = new Formation(formationName, book);

                    formationDao.save(factory, formation);

                    formations.add(formation);
                }


                int j = i + 1;

//                     System.out.println(j + ": " + playbookName + " " + "no of formations: " + formationTotal + "\n" + formations + "\n");
//                     //output.println(j + ": " + playbookName + " " + "no of formations: " + formationTotal + "\n" + formations + "\n");
//
//                     System.out.println( mapSearch(teamName).toString());

            } catch (org.openqa.selenium.NoSuchElementException exc) {

                continue;

            }

            driver.navigate().back();
        }

     //   factory.close();

        driver.quit();

        return playBookMap;
    }


    public WebDriver createDriver() {

        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors",
                "--disable-extensions", "--no-sandbox", "--disable-dev-shm-usage");


        return new ChromeDriver();
    }

    public String saveImage(String address, String imageName, int i) {

        //   System.out.println(address);

        File file = null;

        BufferedImage image = null;

        URL url = null;

        try {

            url = new URL(address);

            image = ImageIO.read(url);

            file = new File("C://Users/qmccl/Programs_By_Language/Java/JavaFx/PlaybookDbFiller/src/main/resources/Images/Teams/" + imageName + ".png");

            file.createNewFile();

            ImageIO.write(image, "png", file);

        } catch (IOException e) {
//                WebDriver driver = createDriver();
//
//                driver.get(teamImages);
//
//                address = driver.findElement(By.cssSelector("//main[@id='main']/article//div[@class='entry-content']/div[5]/figure["+(i+1)+"]")).findElement(By.cssSelector("src")).getText();
//
//                saveImage(address, imageName, i);
        }

        // System.out.println(file);
        return file.getName();
    }

    public String savePlayImage(String address, String imageName, String playType) {

        //   System.out.println(address);

        File file = null;

        BufferedImage image = null;

        URL url = null;

        try {

            String formedAddress = address.substring(address.indexOf("(")+2,address.indexOf(")")-1);

            url = new URL(formedAddress);

            image = ImageIO.read(url);

            Files.createDirectories(Paths.get("C://Users/qmccl/Programs_By_Language/Java/JavaFx/PlaybookDbFiller/src/main/resources/Images/"+playType+"_Plays/"));

            file = new File("C://Users/qmccl/Programs_By_Language/Java/JavaFx/PlaybookDbFiller/src/main/resources/Images/"+playType+"_Plays/" + imageName + ".png");

            file.createNewFile();

            ImageIO.write(image, "png", file);

        } catch (IOException e) {

            e.printStackTrace();

        }

        // System.out.println(file);
        return file.getName();
    }

    public List<WebElement> choosePlayBooks(WebDriver driver, String playbooksType) {

        List<WebElement> playbookLinks = new ArrayList<>();


        if (playbooksType.toUpperCase().equals("OFFENSE")) {

            //Get list of all playbook links
            playbookLinks = driver.findElement(By.cssSelector(".l-sidebar__main > ul:nth-of-type(1)")).findElements(By.cssSelector("li"));


        } else if (playbooksType.toUpperCase().equals("DEFENSE")) {

            //Get list of all playbook links
            playbookLinks = driver.findElement(By.cssSelector(".l-sidebar__main > ul:nth-of-type(2)")).findElements(By.cssSelector("li"));

        }

        return playbookLinks;
    }

    public Team teamMapSearch(String teamName) {

        for (Map.Entry<String, Team> entry : teamsMap.entrySet())
            if (entry.getKey().contains(teamName)) {
                return entry.getValue();
            }

        return null;

    }

    public PlayBook playbookMapSearch(String playbookName) {

        for (Map.Entry<String, PlayBook> entry : playBookMap.entrySet())
            if (entry.getKey().toUpperCase().contains(playbookName)) {

                return entry.getValue();

            }

        return null;

    }


//    public ArrayList<Formation> formationMapSearch(String playbookName) {
//
//        ArrayList<Formation> formations = new ArrayList<>();
//
//        for (Map.Entry<String, ArrayList<Formation>> entry : formationMap.entrySet())
//            if (entry.getKey().contains(playbookName)) {
//
//                formations.addAll(entry.getValue());
//
//                return formations;
//            }
//
//        return null;
//
//    }
//
//    public ArrayList<Scheme> schemeMapSearch(String formationName) {
//
//        ArrayList<Scheme> schemes = new ArrayList<>();
//
//        for (Map.Entry<String, ArrayList<Scheme>> entry : schemeMap.entrySet())
//            if (entry.getKey().contains(formationName)) {
//
//                schemes.addAll(entry.getValue());
//
//                return schemes;
//            }
//
//        return null;
//
//    }

    public ArrayList<Play> playMapSearch(String schemeName) {

        ArrayList<Play> plays = new ArrayList<>();

        for (Map.Entry<String, ArrayList<Play>> entry : playMap.entrySet())
            if (entry.getKey().contains(schemeName)) {

                plays.addAll(entry.getValue());

                return plays;
            }

        return null;

    }

    public void loadPlays(String playbookType) {


        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Team.class)
                .addAnnotatedClass(PlayBook.class)
                .addAnnotatedClass(Coach.class)
                .addAnnotatedClass(Formation.class)
                .addAnnotatedClass(Play.class)
                .addAnnotatedClass(Scheme.class)
                .buildSessionFactory();

        SchemeDao schemeDao = new SchemeDao();
        PlayBookDao playBookDao = new PlayBookDao();
        FormationDao formationDao = new FormationDao();
        String teamPlaybooks = "https://huddle.gg/23/playbooks/";
        List<Play> plays = new ArrayList<>();
        List<WebElement> schemeLinks;
        List<WebElement> playLinks;
        List<PlayBook> playBooks = playBookDao.findAllByPlayType(factory,playbookType);
        List<Formation> formations;
        List<Scheme> schemes;
        String playName;
        String playImage;
        int schemeTotal;

        PlayDao playDao = new PlayDao();




        int playLinksSize;

        playBookMap = new HashMap<>();

        schemeMap = new HashMap<>();



        for(PlayBook book : playBooks){

            playBookMap.put(book.getName(), book);
        }

        for (int pb = 0; pb <= 31; pb++) {//change to 0

            WebDriver driver = createDriver();

            driver.get(teamPlaybooks);

            //Get list of all playbook links
            playBookLinks = choosePlayBooks(driver, playbookType);


            playBookLinks.get(pb).click();

            //Get Playbook name
            String playbookName = driver.findElement(By.xpath("/html//div[@class='body-content']/div//h1[@class='heading heading--blue']")).getText();

           playbookName= playbookName.replaceAll("\\s.*", "");


            System.out.println("1. "+playbookName);
             PlayBook book = playbookMapSearch(playbookName);

            System.out.println( playBookMap.containsKey(playbookName));


           formations = formationDao.findAllByPlaybook(factory, book);


            int formationNum = driver.findElement(By.cssSelector(".l-sidebar__main > .py-5.wrapper")).findElements(By.cssSelector("h3")).size();


            formationMap = new HashMap<>();

            for(Formation formation : formations){

                formationMap.put(formation.getName(), formation);
            }




            for (int f = 1; f <= formationNum - 1; f++) {//change to 1

                driver.navigate().refresh();

                String formationName = driver.findElement(By.cssSelector(".py-5.wrapper > h3:nth-of-type(" + f + ")")).getText();

                schemes = schemeDao.findAllByFormation(factory,formationMap.get(formationName.toUpperCase()));

                schemeTotal = driver.findElement(By.cssSelector(".py-5.wrapper > ul:nth-of-type(" + f + ")")).findElements(By.cssSelector("li")).size();

                System.out.println("1. Formation: " + formationName);
                System.out.println("2. num of schemes " + schemeTotal);

                for(Scheme scheme: schemes){

                    schemeMap.put(scheme.getName(), scheme);
                }

                Scheme scheme;

                    for (int s = 0; s <= schemeTotal - 1; s++) {//change to 0

                    driver.navigate().refresh();

                        String schemeName;


                        //number of unordered lists formations
                        schemeLinks = driver.findElement(By.cssSelector(".py-5.wrapper > ul:nth-of-type(" + f + ")")).findElements(By.cssSelector("li"));

                        schemeLinks.get(s).click();

                        schemeName = driver.findElement(By.cssSelector(".heading.heading--blue")).getText();

                        System.out.println(schemeName);

                        scheme = schemeMap.get(schemes.get(s).getName().toUpperCase());
                          // scheme = new Scheme(schemeName, playbookType, formationMap.get(formationName));

                        playLinksSize = driver.findElement(By.cssSelector(".play-tile-list")).findElements(By.cssSelector("a")).size();

                        System.out.println("Num of plays" + playLinksSize);

                       // scheme = schemeDao.saveFlush(factory, scheme);

                        System.out.println(scheme);
                        for (int p = 0; p <= playLinksSize - 1; p++) {



                            //Get all scheme links
                            playLinks = driver.findElement(By.cssSelector(".play-tile-list")).findElements(By.cssSelector("a"));

                            playLinks.get(p).click();

                            playName = driver.findElement(By.cssSelector(".heading.heading--blue")).getText(); //change number variable

                            System.out.println(playName);

                            playImage = driver.findElement(By.cssSelector(".play-tile > .play-tile__image")).getCssValue("background-image");

                           // System.out.println(playImage);

                             playImage = savePlayImage(playImage, playName, playbookType);

                             Play play = new Play(playName, null, null, playImage, scheme);

                              playDao.save(factory, play);

                              plays.add(play);

                            driver.navigate().back();

                        }//play


                        //  playMap.put(scheme.getName(), plays);

                        driver.navigate().back();

                    }//scheme
                    //
//                } catch (Exception exc) {
//
//                    exc.printStackTrace();
//                    System.out.println("F= "+f);
//                    continue;
//                }


            }//formation

//            }catch (Exception exc) {
//
//               // exc.printStackTrace();
//                driver.navigate().back();
//                continue;
//
//
//            }

          //  driver.navigate().back();

        driver.close();

        }//playbook
       // driver.quit();
    }

    public void loadSchemes( String playbookType) {


        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Team.class)
                .addAnnotatedClass(PlayBook.class)
                .addAnnotatedClass(Coach.class)
                .addAnnotatedClass(Formation.class)
                .addAnnotatedClass(Play.class)
                .addAnnotatedClass(Scheme.class)
                .buildSessionFactory();

        SchemeDao schemeDao = new SchemeDao();
        PlayBookDao playBookDao = new PlayBookDao();
        FormationDao formationDao = new FormationDao();
        String teamPlaybooks = "https://huddle.gg/23/playbooks/";
        List<Play> plays = new ArrayList<>();
        List<WebElement> schemeLinks;
        List<WebElement> playLinks;
        List<PlayBook> playBooks = playBookDao.findAllByPlayType(factory, playbookType);
        List<Formation> formations;
        List<Scheme> schemes;
        String playName;
        String playImage;
        int schemeTotal;

        PlayDao playDao = new PlayDao();

        WebDriver driver = createDriver();


        int playLinksSize;

        playBookMap = new HashMap<>();

        schemeMap = new HashMap<>();



        for(PlayBook book : playBooks){

            playBookMap.put(book.getName(), book);
        }

        for (int pb = 0; pb <= 31; pb++) {//change to 0

            driver.navigate().refresh();

            driver.get(teamPlaybooks);

            //Get list of all playbook links
            playBookLinks = choosePlayBooks(driver, playbookType);


            playBookLinks.get(pb).click();

            //Get Playbook name
            String playbookName = driver.findElement(By.xpath("/html//div[@class='body-content']/div//h1[@class='heading heading--blue']")).getText();

            playbookName= playbookName.replaceAll("\\s.*", "");


            System.out.println("1. "+playbookName);
            PlayBook book = playbookMapSearch(playbookName);

            System.out.println( playBookMap.containsKey(playbookName));


            formations = formationDao.findAllByPlaybook(factory, book);

            System.out.println(formations);

            int formationNum = driver.findElement(By.cssSelector(".l-sidebar__main > .py-5.wrapper")).findElements(By.cssSelector("h3")).size();


            formationMap = new HashMap<>();

            for(Formation formation : formations){

                formationMap.put(formation.getName(), formation);
            }




            for (int f = 1; f <= formationNum - 1; f++) {//change to 1

                driver.navigate().refresh();

                String formationName = driver.findElement(By.cssSelector(".py-5.wrapper > h3:nth-of-type(" + f + ")")).getText();

               // schemes = schemeDao.findAllByFormation(factory,formationMap.get(formationName.toUpperCase()));

                schemeTotal = driver.findElement(By.cssSelector(".py-5.wrapper > ul:nth-of-type(" + f + ")")).findElements(By.cssSelector("li")).size();

                System.out.println("1. Formation: " + formationName);
                System.out.println("2. num of schemes " + schemeTotal);

//                for(Scheme scheme: schemes){
//
//                    schemeMap.put(scheme.getName(), scheme);
//                }

                Scheme scheme;

                for (int s = 0; s <= schemeTotal - 1; s++) {//change to 0

                    driver.navigate().refresh();

                    String schemeName;


                    //number of unordered lists formations
                    schemeLinks = driver.findElement(By.cssSelector(".py-5.wrapper > ul:nth-of-type(" + f + ")")).findElements(By.cssSelector("li"));

                    schemeLinks.get(s).click();

                    schemeName = driver.findElement(By.cssSelector(".heading.heading--blue")).getText();

                    System.out.println(schemeName);

                    System.out.println(formationMap.get(formationName.toUpperCase()));

                    //scheme = schemeMap.get(schemes.get(s).getName().toUpperCase());
                    scheme = new Scheme(schemeName, playbookType, formationMap.get(formationName.toUpperCase()));

                    schemeDao.saveFlush(factory, scheme);

                //    playLinksSize = driver.findElement(By.cssSelector(".play-tile-list")).findElements(By.cssSelector("a")).size();

                 //   System.out.println("Num of plays" + playLinksSize);

                 //   scheme = schemeDao.saveFlush(factory, scheme);

                    driver.navigate().back();
                        }


                }

                driver.navigate().back();


        }

        driver.close();

        driver.quit();

    }

    public void loadFormations( String playBookType) {


        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Team.class)
                .addAnnotatedClass(PlayBook.class)
                .addAnnotatedClass(Coach.class)
                .addAnnotatedClass(Formation.class)
                .addAnnotatedClass(Play.class)
                .addAnnotatedClass(Scheme.class)
                .buildSessionFactory();

        int formationTotal = 0;
        String playbookName = null;
        String formationName;
        List<WebElement> playBookLinks = null;
        FormationDao formationDao = new FormationDao();
        formationMap = new HashMap<>();
        ArrayList<Formation> formations = null;

        WebDriver driver = createDriver();

        driver.get(teamPlaybooks);

        int offTotal = 32;

        for (int i = 0; i <= offTotal - 1; i++) {



                //Get list of all playbook links
                playBookLinks = choosePlayBooks(driver, playBookType);

                formations = new ArrayList<>();

                playBookLinks.get(i).click();

                //Get Playbook name
                playbookName = driver.findElement(By.cssSelector(".heading.heading--blue")).getText();


                PlayBook book = playbookMapSearch(playbookName);

                //Getting the number of formations on page
                formationTotal = driver.findElement(By.xpath("/html//div[@class='body-content']/div//div[@class='l-sidebar__main']/div")).findElements(By.cssSelector("h3")).size();

//                       System.out.println(formationTotal);
            try {
                for (int f = 1; f <= formationTotal; f++) {
                    //Getting formation name
                    formationName = driver.findElement(By.cssSelector(".py-5.wrapper > h3:nth-of-type(" + f + ")")).getText();

                    Formation formation = new Formation(formationName, book);

                    formationDao.save(factory, formation);

                    formations.add(formation);
                }


            } catch (Exception exc) {
                exc.printStackTrace();
            }

          //  formationMap.put(playbookName, formations);

            driver.navigate().back();
        }

    }
}



