package ohtu;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.io.IOException;
import java.util.Scanner;
import org.apache.http.client.fluent.Request;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {

    public static void main(String[] args) throws IOException {
        // vaihda oma opiskelijanumerosi seuraavaan, ÄLÄ kuitenkaan laita githubiin omaa opiskelijanumeroasi
        Scanner scan = new Scanner(System.in);
        String studentNr = scan.next();
        scan.close();


        if ( args.length>0) {
            studentNr = args[0];
        }

        String url = "https://studies.cs.helsinki.fi/ohtustats/students/"+studentNr+"/submissions";

        String bodyText = Request.Get(url).execute().returnContent().asString();

        String url2 = "https://studies.cs.helsinki.fi/ohtustats/courseinfo";

        String bodyText2 = Request.Get(url2).execute().returnContent().asString();        
        


        String statsResponse = Request.Get("https://studies.cs.helsinki.fi/ohtustats/stats").execute().returnContent().asString();

        JsonParser parser = new JsonParser();
        JsonObject parsittuData = parser.parse(statsResponse).getAsJsonObject();        
        
        int i=1;
        int pa=0;
        int mon=0;
        JsonObject l = parsittuData.getAsJsonObject(""+i);
        

        while(l!=null){
            JsonElement l2= l.get("exercise_total");
            JsonArray l3= l.getAsJsonArray("exercises");
            pa+=l2.getAsInt();
            mon+=l3.size();
            i++;
            l = parsittuData.getAsJsonObject(""+i);

        }

        

        Gson mapper = new Gson();
        Submission[] subs = mapper.fromJson(bodyText, Submission[].class);
        Kurssi ohtu=mapper.fromJson(bodyText2, Kurssi.class);
        int yht=0;
        int yhttunt=0;
        System.out.println("Kurssi: "+ohtu.getName()+" "+ohtu.getTerm());
        System.out.println("opiskelijanumero: "+studentNr);
        for (Submission submission : subs) {
            System.out.println(submission.toString());
            yht+=submission.getExercises().length;
            yhttunt+=submission.getHours();
        }
        System.out.println("yhteensä: "+yht+" tehtävää "+yhttunt+" tuntia");
        System.out.println("kurssilla yhteensä "+mon+" palautusta, palautettuja tehtäviä "+pa+" kpl");
    }
}