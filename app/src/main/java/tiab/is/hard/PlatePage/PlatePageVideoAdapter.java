package tiab.is.hard.PlatePage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;
import tiab.is.hard.R;

import static tiab.is.hard.MainActivity.print;

public class PlatePageVideoAdapter extends RecyclerView.Adapter<PlatePageVideoAdapter.ViewHolder> {

    private List<PlatePage.Video> mitems;
    private LayoutInflater mInflater;
    private YouTubePlayer.OnInitializedListener mOnInitializeListener;
    String pls_net;

    class ViewHolder extends RecyclerView.ViewHolder {

        YouTubePlayerView video;
        YouTubeThumbnailView show_episode_thumbnail;

        ViewHolder(View itemView) {
            super(itemView);
            show_episode_thumbnail = itemView.findViewById(R.id.show_episode_thumbnail);
            video = itemView.findViewById(R.id.video);
        }
    }

    PlatePageVideoAdapter(Context context, List<PlatePage.Video> items) {
        mitems = items;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PlatePageVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView = mInflater.inflate(R.layout.videos_of_a_plate_sample, parent, false);
        return new PlatePageVideoAdapter.ViewHolder(contactView); }
    @Override
    public int getItemCount() { return mitems.size(); }


    @Override
    public void onBindViewHolder(@NonNull final PlatePageVideoAdapter.ViewHolder viewHolder, final int position) {
        final PlatePage.Video item = mitems.get(position);
        initialize_listener(viewHolder, item.LINK);

        viewHolder.show_episode_thumbnail.initialize(YoutubeConfig.getApiKey(), new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(item.LINK);
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                        viewHolder.show_episode_thumbnail.setOnClickListener(v -> play_video(viewHolder));
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }


    private void play_video(@NonNull final PlatePageVideoAdapter.ViewHolder viewHolder){
        viewHolder.video.initialize(YoutubeConfig.getApiKey(), mOnInitializeListener);
        viewHolder.show_episode_thumbnail.setVisibility(View.INVISIBLE);
    }


    private void initialize_listener(@NonNull final PlatePageVideoAdapter.ViewHolder viewHolder, String videolink){
        mOnInitializeListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videolink);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                pls_net = viewHolder.itemView.getContext().getString(R.string.pls_net);
                print(viewHolder.itemView.getContext(), pls_net, 1);
            }
        };
    }

}