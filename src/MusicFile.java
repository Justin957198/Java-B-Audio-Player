import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;


public class MusicFile {
    File[] playList;
    double songLength;
    String filePath = ".\\src\\MP3 Directory\\";
    public static int songAmount;
    public MusicFile()
    {
        playList = pathGetter(filePath);
    }

    public File[] pathGetter(String folderDir)
    {
        File folder = new File(folderDir);
        if(folder.isDirectory())
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
        else {
            try {
                String dirPath = "MP3 Storage\\MP3 Directory";
                Files.createDirectories(Paths.get(dirPath));
                filePath = folderDir;
                playList = pathGetter(dirPath);
                return  playList;
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return null;
    }

    public String getPlayList()
    {
        StringBuilder format = new StringBuilder();
        ArrayList<String> songList = new ArrayList<>();
        songAmount = playList.length;
        for(int i = 0; i < playList.length; i++)
        {
            songList.add(playList[i].getName());
        }

        for(String songName : songList)
        {
            format.append(songName).append(System.lineSeparator());
        }
        String songs = format.toString();
        return songAmount + "-Starts at 0" + "\n" + songs;
    }

    public String deleteSong(String songNumber)
    {
        int songFinder = 0;
        String target = ".\\src\\Trash\\";
        try {
            songFinder = Integer.parseInt(songNumber);
        } catch (NumberFormatException e) {
            return "Enter song number";
        }
        String fileName = playList[songFinder].getName();
        File delete = new File(target + fileName);
        try {
            delete.createNewFile();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Path selected = Path.of(playList[songFinder].getAbsolutePath());

        try {
            Files.move(selected, delete.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.delete(delete.toPath());
            playList = pathGetter(filePath);
            return "Song removed";

        } catch (IOException ex) {
            return "Error";
        }

    }



    public String[] songNameGetter(String folderDir, int songNum)
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
        File folder = new File("MP3 Storage\\MP3 Directory");
        if(folder.isDirectory())
        {
            String[] seporatedTitle = null;
            File[] musCollection = folder.listFiles();
            if(musCollection.length == 0)
            {
                seporatedTitle = new String[2];
                seporatedTitle[0] = "No songs to play";
                seporatedTitle[1] = "Try adding songs";
                return  seporatedTitle;
            }
            if(musCollection[songNum].isFile())
            {
                songName = musCollection[songNum].getName();
            }
            if(songName.contains("-"))
            {
                seporatedTitle = songName.split("-");
            }
            else {
                seporatedTitle = new String[2];
                seporatedTitle[0] = "Unknown Artist";
                seporatedTitle[1] = songName;
            }
            playList = pathGetter(filePath);
            return seporatedTitle;

        }
        return null;
    }
}
