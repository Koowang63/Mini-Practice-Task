public abstract class ContentItem {
    protected int id;
    protected static int idGen = 1;
    protected String title;
    protected int year;
    protected int durationMinutes;

    public ContentItem(String title, int year, int durationMinutes) {
        this.id = idGen++;
        setTitle(title);
        setYear(year);
        setDurationMinutes(durationMinutes);
    }
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public int getYear() {
        return year;
    }
    public int getDurationMinutes() {
        return durationMinutes;
    }
    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be null or blank");
        }
        this.title = title;
    }

    public void setYear(int year) {
        int currentYear = Year.now().getValue();
        if (year < 1990 || year > currentYear) {
            throw new IllegalArgumentException("Year must be between 1990 and current year");
        }
        this.year = year;
    }
    public void setDurationMinutes(int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
        this.durationMinutes = durationMinutes;
    }

    public int getAge(int currentYear) {
        return currentYear - year;
    }
    public abstract double getLicenseCost(int currentYear);

    public String toString() {
        return "ContentItem{id=" + id +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", durationMinutes=" + durationMinutes + '}';
    }
}


public interface Downloadable {
    void download();
    int getMaxDownloadsPerDay();
}


public class VideoLecture extends ContentItem implements Downloadable {

    private String quality;

    public VideoLecture(String title, int year, int durationMinutes, String quality) {
        super(title, year, durationMinutes);
        setQuality(quality);
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        if (quality == null || quality.isBlank()) {
            throw new IllegalArgumentException("Quality must not be empty");
        }
        this.quality = quality;
    }

    @Override
    public double getLicenseCost(int currentYear) {
        int age = getAge(currentYear);
        int ageFactor = (age <= 2) ? 5 : 2;
        return 0.05 * durationMinutes + ageFactor;
    }

    @Override
    public void download() {
        System.out.println("Downloading video in " + quality + "...");
    }

    @Override
    public int getMaxDownloadsPerDay() {
        return 3;
    }

    @Override
    public String toString() {
        return super.toString() + ", quality=" + quality;
    }
}


public class PodcastEpisode extends ContentItem implements Downloadable {

    private String hostName;
    public PodcastEpisode(String title, int year, int durationMinutes, String hostName) {
        super(title, year, durationMinutes);
        setHostName(hostName);
    }

    public String getHostName() {
        return hostName;
    }
    public void setHostName(String hostName) {
        if (hostName == null || hostName.isBlank()) {
            throw new IllegalArgumentException("Host name must not be empty");
        }
        this.hostName = hostName;
    }
    @Override
    public double getLicenseCost(int currentYear) {
        int age = getAge(currentYear);
        int ageFactor = (age <= 2) ? 3 : 1;
        return 0.03 * durationMinutes + ageFactor;
    }
    @Override
    public void download() {
        System.out.println("Downloading podcast hosted by " + hostName + "...");
    }

    @Override
    public int getMaxDownloadsPerDay() {
        return 10;
    }

    @Override
    public String toString() {
        return super.toString() + ", hostName=" + hostName;
    }
}


public class ContentDemo {
    public static void main(String[] args) {
        List<ContentItem> items = new ArrayList<>();

        items.add(new VideoLecture("Java OOP Basics", 2024, 90, "HD"));
        items.add(new VideoLecture("Advanced Java", 2022, 120, "4K"));

        items.add(new PodcastEpisode("Tech Talks", 2023, 45, "Alice"));
        items.add(new PodcastEpisode("Coding Life", 2021, 60, "Bob"));

        int currentYear = Year.now().getValue();
        for (ContentItem item : items) {
            System.out.println(item +
                    " | licenseCost=" +
                    item.getLicenseCost(currentYear));
            if (item instanceof Downloadable downloadable) {
                downloadable.download();
                System.out.println("Max downloads/day: " +
                        downloadable.getMaxDownloadsPerDay());
            }
            System.out.println("----------------------------------");
        }
    }
}