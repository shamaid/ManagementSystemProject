package Domain;


import Service.Server;

public class IdGenerator {

    private static int nextId =0;

    //each time the server is up
    public static void setNextId(int id){
        nextId = id;
    }

    public static int getNewId(){
        nextId = nextId + 1;
        Server.updateID(nextId);
        return nextId;
    }

}
