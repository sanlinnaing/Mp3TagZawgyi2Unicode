package com.mua.sanlin;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.io.TextEncoding;
import org.blinkenlights.jid3.v1.ID3V1Tag;
import org.blinkenlights.jid3.v2.ID3V2Tag;
import com.comquas.rabbit.Rabbit;

public class convertZ2U {
	static int count = 0;
	static int fileCount = 0;
	static File[] toConvertFiles;
	static boolean convert = false;

	public static void main(String[] argv) throws IOException {
		TextEncoding.setDefaultTextEncoding(TextEncoding.UNICODE);
		if (argv.length < 1) {
			System.err.println("Full File Path required!");
			System.out.println("Option");
			System.out
					.println("Mp3Zg2Uni.jar [Full_File_Path_Without_Space] --opt [check OR convert]");
			return;
		}
		if (argv.length == 3) {
			if (argv[1].equals("--opt") && argv[2].equals("check")) {
				convert = false;
			}
			if (argv[1].equals("--opt") && argv[2].equals("convert")) {
				convert = true;
			}
		}
		File folder = new File(argv[0]);
		if (!folder.exists()) {
			System.err.println("File or Folder not found!");
		}
		if (!folder.isDirectory()) {
			File[] listOfFiles = { folder };
			allMp3FileTagger(listOfFiles);

		} else {
			File[] listOfFiles = folder.listFiles(new Mp3Filter());
			allMp3FileTagger(listOfFiles);
		}
		System.out.println("Found " + count + "/" + fileCount + " Files.");
	}

	static void allMp3FileTagger(File[] fileList) {
		if (fileList == null) {
			System.err.println("File permission denied!");
			return;
		}
		for (int i = 0; i < fileList.length; i++) {
			File f = fileList[i];
			if (f.isDirectory()) {
				allMp3FileTagger(f.listFiles(new Mp3Filter()));
				continue;
			}
			fileCount++;
			// System.out.println(f.getPath());
			// File f = folder;
			/*
			 * System.out.println(f.getPath()); System.out
			 * .println(f.renameTo(new File(Rabbit.zg2uni(f.getPath()))));
			 */
			// Files.move(f.toPath(),
			// new File(Rabbit.zg2uni(f.getPath())).toPath(),
			// java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			try {
				MP3File mp3file = new MP3File(f);
				boolean found = false;
				if (mp3file.getID3V1Tag() != null) {
					// System.out.println("ID 3 version 1");
					ID3V1Tag id3v1 = mp3file.getID3V1Tag();
					// System.out.println(id3v1.toString());
					String param = "";
					if ((id3v1.getTitle() != null)
							&& ZgDetector.check(id3v1.getTitle())) {
						param += "Title : [" + id3v1.getTitle() + "] ";
						id3v1.setTitle(Rabbit.zg2uni(id3v1.getTitle()));
						mp3file.setID3Tag(id3v1);

						found = true;
					}
					if (id3v1.getArtist() != null
							&& ZgDetector.check(id3v1.getArtist())) {
						param += "Artist : [" + id3v1.getArtist() + "] ";
						id3v1.setArtist(Rabbit.zg2uni(id3v1.getArtist()));
						mp3file.setID3Tag(id3v1);
						found = true;
					}
					if (id3v1.getAlbum() != null
							&& ZgDetector.check(id3v1.getAlbum())) {
						param += "Album : [" + id3v1.getAlbum() + "]";
						id3v1.setAlbum(Rabbit.zg2uni(id3v1.getAlbum()));
						mp3file.setID3Tag(id3v1);
						found = true;
					}
					if (found) {
						System.out.println(f.getPath()+ "\n"+param);
						// System.out.println(" convert mp3 tag to Unicode.");
						// System.out.println(id3v1.toString());
						count++;
					}
				}

				if (mp3file.getID3V2Tag() != null) {
					String param = "";
					// System.out.println("ID 3 version 2");
					ID3V2Tag id3v1 = mp3file.getID3V2Tag();
					// System.out.println(id3v1.toString());

					if ((id3v1.getTitle() != null)
							&& ZgDetector.check(id3v1.getTitle())) {
						param += "Title : [" + id3v1.getTitle() + "] ";
						id3v1.setTitle(Rabbit.zg2uni(id3v1.getTitle()));
						mp3file.setID3Tag(id3v1);

						found = true;
					}
					if (id3v1.getArtist() != null
							&& ZgDetector.check(id3v1.getArtist())) {
						param += "Artist : [" + id3v1.getArtist() + "] ";
						id3v1.setArtist(Rabbit.zg2uni(id3v1.getArtist()));
						mp3file.setID3Tag(id3v1);
						found = true;
					}
					if (id3v1.getAlbum() != null
							&& ZgDetector.check(id3v1.getAlbum())) {
						param += "Album : [" + id3v1.getAlbum() + "]";
						id3v1.setAlbum(Rabbit.zg2uni(id3v1.getAlbum()));
						mp3file.setID3Tag(id3v1);
						found = true;
					}
					if (found) {
						System.out.println(f.getPath()+ "\n"+param);
						// System.out.println(" convert mp3 tag to Unicode.");
						// System.out.println(id3v1.toString());
						count++;
					}
				}
				if (found && convert) {
					mp3file.sync();
					System.out.println("converted!");
				}
			} catch (ID3Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// System.out.println("File Process Warning!");
			}

		}
	}
}

class Mp3Filter implements FileFilter {

	@Override
	public boolean accept(File file) {
		// TODO Auto-generated method stub

		return file.isDirectory()
				|| file.getName().toLowerCase().endsWith(".mp3");
	}

}
