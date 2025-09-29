import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;


public class MusicFile { // The main class for handling creation of the mp3 directory and playlist
    File[] playList; // the file array for the songs
    double songLength;
    String filePath = ".\\src\\MP3 Directory\\";
    public static int songAmount; // the number of songs
    public MusicFile()
    {
        playList = pathGetter(filePath);
    }

    public File[] pathGetter(String folderDir) // obtains the paths of all the songs in the mp3 directory folder allowing them to be converted to audio files
    {
        File folder = new File(folderDir); // create a file using the passed file path
        if(folder.isDirectory()) // checks if the file is a directory / folder
        {
            File[] musCollection = folder.listFiles();
            File[] musicFiles = new File[musCollection.length];
            int iterator = 0;
            for(File music : musCollection)
            {
                if(music.isFile())
                {
                    musicFiles[iterator] = music.getAbsoluteFile();
                    System.out.println(music.getAbsoluteFile());
                    iterator++;
                }
            }
            return musicFiles;
        }
        else { // if no such directory exists a new folder is created on the same level as the application.
            try {
                String dirPath = "MP3 Storage\\MP3 Directory"; // creates a folder called mp3 storage, then creates an inner folder called mp3 directory
                Files.createDirectories(Paths.get(dirPath));
                filePath = folderDir;
                playList = pathGetter(dirPath); // restarts the function this time with a valid mp3 storage location.
                return  playList; // returns the playlist of music even if there are no songs.
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return null;
    }

    public String getPlayList() // a method used to display all songs in the playlist, connected to the playlist button.
    {
        StringBuilder format = new StringBuilder(); // string builder used to format output.
        ArrayList<String> songList = new ArrayList<>(); // an arraylist to hold the names of the songs.
        songAmount = playList.length; // the amount of songs is taken from the length of the playlist.
        for(int i = 0; i < playList.length; i++)
        {
            songList.add(playList[i].getName()); // iterates through the playlist getting the song name and adding it to the songList arrayList.
        }

        for(String songName : songList)
        {
            format.append(songName).append(System.lineSeparator()); // using string builder the songs will output in a large column not a single row.
        }
        String songs = format.toString(); // the new list formatted
        return songAmount + "-Starts at 0" + "\n" + songs; // the top of the playlist display acknowledge that the counting of songs start at zero not 1.
    }

    public String deleteSong(String songNumber) // unused method from a previous mp3 version, this method would delete a song. This function was removed due to how easily one can delete songs on their own.
    {
        int songFinder = 0;
        String target = ".\\src\\Trash\\"; // the area designated trash, songs will go here before their deleted.
        try {
            songFinder = Integer.parseInt(songNumber); // gets the index of the specific song that needs to be deleted
        } catch (NumberFormatException e) {
            return "Enter song number"; // if the value entered is not a number an error is thrown and the user is asked again.
        }
        String fileName = playList[songFinder].getName(); // gets the name of the song at the index.
        File delete = new File(target + fileName); // creates a new file with the same name as the song file in the trash directory.
        try {
            delete.createNewFile();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Path selected = Path.of(playList[songFinder].getAbsolutePath()); // gets the path of the selected song.

        try {
            Files.move(selected, delete.toPath(), StandardCopyOption.REPLACE_EXISTING); // moves the song from the playlist to the trash directory replacing the newly created text file replica.
            Files.delete(delete.toPath()); // deletes the file within the trash directory
            playList = pathGetter(filePath); // re-initialize the playlist with one less song
            return "Song removed"; // String confirmation.

        } catch (IOException ex) {
            return "Error"; // outputs an error if it went wrong.
        }

    }



    public String[] songNameGetter(String folderDir, int songNum) // gets the name of the song and splits it into the artists name and the song name.
    {
        //playList = pathGetter(filePath);
        String songName = null;
        if(songNum == -1)
        {
            songNum = playList.length-1;
        }
        if(songNum > playList.length-1)
        {
            songNum = 0;
        }
        File folder = new File("MP3 Storage\\MP3 Directory"); // getys the folder / playlist with the songs inside
        if(folder.isDirectory())
        {
            String[] seporatedTitle = null; // initializes empty string array.
            File[] musCollection = folder.listFiles(); // creates a file array from the music in the playlist.
            if(musCollection.length == 0) // if there's nothing in the playlist then the two outputs are changed below.
            {
                seporatedTitle = new String[2];
                seporatedTitle[0] = "No songs to play";
                seporatedTitle[1] = "Try adding songs";
                return  seporatedTitle;
            }
            if(musCollection[songNum].isFile()) // if there is a song at the designated index the entire name of the song is obtained.
            {
                songName = musCollection[songNum].getName();
            }
            if(songName.contains("-")) // the name is searched for the "-" symbol which will allow the name of the song to be split assuming the song title is in the format artist - song name.wav.
            {
                seporatedTitle = songName.split("-");
            }
            else { // if the song artist isn't found then Unknown artist is displayed along with the song name.
                seporatedTitle = new String[2];
                seporatedTitle[0] = "Unknown Artist";
                seporatedTitle[1] = songName;
            }
            playList = pathGetter(filePath);
            return seporatedTitle; // the string array containing the artist and the song name is returned.

        }
        return null;
    }
}
