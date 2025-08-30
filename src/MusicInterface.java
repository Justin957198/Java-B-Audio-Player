import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;

public class MusicInterface extends Thread implements ActionListener, AdjustmentListener, LineListener {
    JFrame MusicBox; JFrame paylistOutput;
    JLabel Title;
    JButton superBack; JButton back;
    JButton forward; JButton superForward;
    JButton play; JButton pause; JButton showPlaylist;
    JButton restart; JButton quit;
    JButton addMusic; JButton deleteSong;
    TextField authorOutput; TextField songTitleOutput; JTextArea playlist;
    TextField number;
    JPanel stripe;
    String filePath = ".\\src\\MP3 Directory\\";
    File[] playList;
    Clip music;
    boolean disAllowAction = false;
    int songNum; double songLength;
    ImageIcon windowImage = new ImageIcon(getClass().getResource("MP3.png"));
    MusicFile manger = new MusicFile();



    public MusicInterface()
    {
        System.out.println(new File("").getAbsolutePath());
        authorOutput = new TextField();
        authorOutput.setBounds(125, 60, 220, 40);
        authorOutput.setFont(new Font("Roboto", Font.PLAIN, 20));
        authorOutput.setBackground(Color.BLACK);
        authorOutput.setForeground(Color.WHITE);
        authorOutput.setEditable(false);

        number = new TextField();
        number.setBounds(350, 60, 40, 40);
        number.setFont(new Font("Roboto", Font.PLAIN, 20));
        number.setBackground(Color.BLACK);
        number.setForeground(Color.WHITE);
        number.setEditable(true);

        songTitleOutput = new TextField();
        songTitleOutput.setBounds(395, 60, 220, 40);
        songTitleOutput.setFont(new Font("Roboto", Font.PLAIN, 15));
        songTitleOutput.setBackground(Color.BLACK);
        songTitleOutput.setForeground(Color.WHITE);

        superBack = new JButton("<---");
        superBack.setBounds(5, 210, 80, 50);
        superBack.setForeground(Color.WHITE);
        superBack.setBackground(Color.BLACK);
        superBack.addActionListener(this);
        superBack.setFocusable(false);

        back = new JButton("<-");
        back.setBounds(85, 210, 80, 50);
        back.setForeground(Color.WHITE);
        back.setBackground(Color.BLACK);
        back.addActionListener(this);
        back.setFocusable(false);

        forward = new JButton("->");
        forward.setBounds(570, 210, 80, 50);
        forward.setForeground(Color.WHITE);
        forward.setBackground(Color.BLACK);
        forward.addActionListener(this);
        forward.setFocusable(false);

        superForward = new JButton("--->");
        superForward.setBounds(650, 210, 80, 50);
        superForward.setForeground(Color.WHITE);
        superForward.setBackground(Color.BLACK);
        superForward.addActionListener(this);
        superForward.setFocusable(false);

        pause = new JButton("||");
        pause.setBounds(240, 120, 80, 50);
        pause.setFocusable(false);
        pause.addActionListener(this);

        play = new JButton("|>");
        play.setBounds(150, 120, 80, 50);
        play.setFocusable(false);
        play.addActionListener(this);

        restart = new JButton("R");
        restart.setBounds(420, 120, 80, 50);
        restart.setFocusable(false);
        restart.addActionListener(this);

        quit = new JButton("Quit");
        quit.setBounds(510, 120, 80, 50);
        quit.setFocusable(false);
        quit.addActionListener(this);


        addMusic = new JButton("Add M");
        addMusic.setBounds(650, 125, 80, 50);
        addMusic.setFocusable(false);
        addMusic.addActionListener(this);

        deleteSong = new JButton("Delete");
        deleteSong.setBounds(10, 125, 80, 50);
        deleteSong.setFocusable(false);
        deleteSong.addActionListener(this);

        showPlaylist = new JButton("playlist");
        showPlaylist.setBounds(650, 65, 80, 50);
        showPlaylist.setFocusable(false);
        showPlaylist.addActionListener(this);

        stripe = new JPanel();
        stripe.setBackground(new Color(150, 30, 60));
        stripe.setBounds(0, 125, 750, 50);
        stripe.setLayout(null);

        Title = new JLabel();
        Title.setFont(new Font("Roboto", Font.BOLD, 20));
        Title.setForeground(Color.BLACK);
        Title.setText("MUSIC MACHINE");
        Title.setVisible(true);
        Title.setBounds(300, 10, 200, 50);

        MusicBox = new JFrame("Music Machine");
        MusicBox.setSize(750, 300);
        MusicBox.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        MusicBox.setResizable(false);
        MusicBox.setVisible(true);
        MusicBox.setLayout(new BorderLayout());
        MusicBox.setIconImage(windowImage.getImage());
        MusicBox.add(stripe);
        stripe.add(Title);
        stripe.add(superBack); stripe.add(back);
        stripe.add(forward); stripe.add(superForward);
        stripe.add(play); stripe.add(pause); stripe.add(showPlaylist);
        stripe.add(restart); stripe.add(quit);
        // stripe.add(addMusic);
        // stripe.add(deleteSong);
        stripe.add(authorOutput); stripe.add(songTitleOutput); stripe.add(number);

        playList = manger.pathGetter(filePath);
        songNum = 0;
    }

    public Clip mp3Space(int songNum) {
        Clip sound = null;
        if(playList == null)
        {
            return sound;
        }
        if(songNum < 0)
        {
            songNum = playList.length;
        }
        if(songNum >= playList.length)
        {
            songNum = 0;
        }
        try (Scanner choice = new Scanner(System.in); AudioInputStream audioStream = AudioSystem.getAudioInputStream(playList[songNum])) {
            sound = AudioSystem.getClip();
            sound.open(audioStream);
            int frameNum = sound.getFrameLength();
            double rate = sound.getFormat().getFrameRate();
            songLength = frameNum / rate;
            System.out.println(songLength);
            System.out.println(songNum);



        } catch (UnsupportedAudioFileException b) {
            System.out.println("Audio not supported, skipping");
            songNum++;
            mp3Space(songNum);
            String[] songTitle = manger.songNameGetter(filePath, songNum);
            if(songTitle[0] == null)
            {
                authorOutput.setText("None");
                songTitleOutput.setText(songTitle[1]);
            }
            else {
                authorOutput.setText(songTitle[0]);
                songTitleOutput.setText(songTitle[1]);
            }
        } catch (IOException b) {
            System.out.println("Files actually not found");
            if(playList.length == 0)
            {
                authorOutput.setText("No songs left");
                songTitleOutput.setText("No songs to be played");
                playList = null;
            }
            else {
                songNum++;
                mp3Space(songNum);
                String[] songTitle = manger.songNameGetter(filePath, songNum);
                if (songTitle[0] == null) {
                    authorOutput.setText("None");
                    songTitleOutput.setText(songTitle[1]);
                } else {
                    authorOutput.setText(songTitle[0]);
                    songTitleOutput.setText(songTitle[1]);
                }
            }
        } catch (LineUnavailableException b) {
            System.out.println("...");
        } catch (IndexOutOfBoundsException b) {
            System.out.println("Out of bounds of list");
            if(playList == null || playList.length == 0)
            {
                authorOutput.setText("Try adding songs");
                songTitleOutput.setText("No songs to play");
            }
        } catch (NullPointerException b) {
            System.out.println("Song doesnt exist");
        }
        return sound;
    }




    @Override
    public void actionPerformed(ActionEvent e) {
        if(playList == null)
        {
            disAllowAction = true;
        }
        if(music == null) {

            if (!disAllowAction) {
                music = mp3Space(songNum);
                String[] songTitle = manger.songNameGetter(filePath, songNum);
                if(songTitle[0] == null)
                {
                    authorOutput.setText("None");
                    songTitleOutput.setText(songTitle[1]);
                }
                else {
                    authorOutput.setText(songTitle[0]);
                    songTitleOutput.setText(songTitle[1]);
                }
            }
        }
        if(e.getSource() == play)
        {
            if(music == null)
            {
                authorOutput.setText("Try adding songs");
                songTitleOutput.setText("No songs to play");
            }
            else {
                music.start();
                music.addLineListener(this);
            }
        }
        else if(e.getSource() == pause)
        {
            if(music == null)
            {
                authorOutput.setText("Try adding songs");
                songTitleOutput.setText("No songs to play");
            }
            else {
                music.stop();
            }
        }
        else if(e.getSource() == restart)
        {
            if(music == null)
            {
                authorOutput.setText("Try adding songs");
                songTitleOutput.setText("No songs to play");
            }
            else {
                music.stop();
                music.setFramePosition(0);
                music.start();
            }
        }
        else if(e.getSource() == quit)
        {
            System.exit(0);
        }
        else if(e.getSource() == forward)
        {
            if (!disAllowAction) {
                songNum++;
                authorOutput.setText(null);
                songTitleOutput.setText(null);
                if(music != null) {
                    music.stop();
                    music.flush();
                }
                music = mp3Space(songNum);
                String[] songTitle = manger.songNameGetter(filePath, songNum);
                if(songNum >= playList.length)
                {
                    songNum = 0;
                }
                if(songTitle[0] == null)
                {
                    authorOutput.setText("None");
                    songTitleOutput.setText(songNum + " " + songTitle[1]);
                }
                else {
                    authorOutput.setText(songTitle[0]);
                    songTitleOutput.setText(songNum + " " + songTitle[1]);
                }
                if(music == null)
                {
                    songNum = 0;
                    music = mp3Space(songNum);
                    songTitle = manger.songNameGetter(filePath, songNum);
                    authorOutput.setText(songTitle[0]);
                    songTitleOutput.setText(songNum + " " + songTitle[1]);
                }
                else {
                    music.start();
                    music.addLineListener(this);
                }
            }

        }
        else if(e.getSource() == back)
        {
            if (!disAllowAction) {
                authorOutput.setText(null);
                songTitleOutput.setText(null);
                songNum--;
                if(songNum == -1)
                {
                    songNum = playList.length-1;
                }
                if(music != null) {
                    music.stop();
                    music.flush();
                }
                music = mp3Space(songNum);
                String[] songTitle = manger.songNameGetter(filePath, songNum);
                if(songTitle[0] == null)
                {
                    authorOutput.setText("None");
                    songTitleOutput.setText(songNum + " " + songTitle[1]);
                }
                else {
                    authorOutput.setText(songTitle[0]);
                    songTitleOutput.setText(songNum + " " + songTitle[1]);
                }
                if(music == null)
                {
                    songNum = playList.length-1;
                    music = mp3Space(songNum);
                    songTitle = manger.songNameGetter(filePath, songNum);
                    if(songTitle[0] == null)
                    {
                        authorOutput.setText("None");
                        songTitleOutput.setText(songNum + " " + songTitle[1]);
                    }
                    else {
                        authorOutput.setText(songTitle[0]);
                        songTitleOutput.setText(songNum + " "+ songTitle[1]);
                    }
                }
                else {
                    music.start();
                    music.addLineListener(this);
                }
            }

        }
        else if(e.getSource() == addMusic)
        {
            JFileChooser chooser = new JFileChooser();
            int action = chooser.showOpenDialog(null);
            String target = ".\\src\\MP3 Directory\\";
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Wav music files (*.wav)", "wav");
            chooser.setFileFilter(filter);

            if(action == JFileChooser.APPROVE_OPTION)
            {

                String fileName = chooser.getSelectedFile().getName();
                String newFileName = fileName.replaceAll("wav", "txt");
                System.out.println(target + newFileName);

                File replace = new File(target + fileName);
                try {
                    replace.createNewFile();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Path selected = Path.of(chooser.getSelectedFile().getAbsolutePath());

                try {
                    Files.move(selected, replace.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    authorOutput.setText("Song Added");

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                playList = manger.pathGetter(filePath);

            }
            else {
                authorOutput.setText("nothing added");
                 }

        }
        if(e.getSource() == showPlaylist)
        {
            authorOutput.setText(null); songTitleOutput.setText(null);
            playlist = new JTextArea(10, 30);
            JScrollPane scroller = new JScrollPane(playlist);
            scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED); scroller.setBounds(0,0,10,480);
            playlist.setBackground(Color.BLACK); playlist.setForeground(Color.WHITE); playlist.setBounds(10,0,350, 500);
            paylistOutput = new JFrame("Playlist");
            paylistOutput.setSize(300, 500); paylistOutput.setDefaultCloseOperation(1); paylistOutput.add(scroller);

            paylistOutput.setVisible(true);
            playlist.setText(manger.getPlayList().toString());



        }
        else if(e.getSource() == deleteSong)
        {

            songTitleOutput.setText(null);
            authorOutput.setText("Enter song Number ->");
            String success = manger.deleteSong(number.getText());
            authorOutput.setText(success);
        }
    }


    @Override
    public void update(LineEvent event) {
        if(LineEvent.Type.STOP == event.getType() && music.getFramePosition() > music.getFrameLength()-1)
        {
            music.close();
            forward.doClick();
            System.out.println("on to the next");
        }
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {

    }
}
