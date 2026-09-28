package ConexaoBanco;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConexaoDB {

    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseReference;

    public static DatabaseReference conectar() {

        if (databaseReference == null) {

            firebaseDatabase = FirebaseDatabase.getInstance();


            databaseReference = firebaseDatabase.getReference();
        }

        return databaseReference;
    }
}
