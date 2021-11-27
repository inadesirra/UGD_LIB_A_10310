package com.inadesirrasukma.ugd_lib_a_10310.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.inadesirrasukma.ugd_lib_a_10310.model.Game;
import com.inadesirrasukma.ugd_lib_a_10310.model.PurchasedGame;
import com.inadesirrasukma.ugd_lib_a_10310.model.Response;
import com.inadesirrasukma.ugd_lib_a_10310.model.UserProfile;
import com.inadesirrasukma.ugd_lib_a_10310.repositories.MainRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private MainRepository repo;

    private MutableLiveData<List<Game>> gameListLiveData;
    private MutableLiveData<String> errMsg;

    private MutableLiveData<List<PurchasedGame>> keranjangBelanja;

    private MutableLiveData<UserProfile> userProfileLiveData;

    {
        gameListLiveData = new MutableLiveData<>(new ArrayList<>());
        keranjangBelanja = new MutableLiveData<>(new ArrayList<>());
        userProfileLiveData = new MutableLiveData<>(null);
        errMsg = new MutableLiveData<>("");
    }

    @Inject
    public MainViewModel(MainRepository repo) {
        this.repo = repo;
    }

    public void loadResponse() {
        try {
            if (this.gameListLiveData.getValue().size() == 0) {
                LiveData<Response> temp = this.repo.getResponse();

                if (temp == null || temp.getValue() == null || temp.getValue().getData().isEmpty())
                    throw new Exception("Data gagal diambil");

                this.gameListLiveData.setValue(temp.getValue().getData());
                this.errMsg.setValue(temp.getValue().getMessage() + " size: " + temp.getValue().getData().size());
            }

        } catch (Exception e) {
            e.printStackTrace();
            errMsg.setValue(e.getMessage());
        }
    }

    /**
     * TODO 3.1 isi logic untuk method scanQRUserProfile()
     * @param jsonString
     * @throws Exception
     * @HINT: gunakan GSON untuk parse jsonString dengan kelas model UserProfile lalu tampung di
     * userProfileLiveData
     */
    public void scanQRUserProfile(String jsonString) throws Exception {
        Gson gson = new Gson();
        UserProfile userProfile = gson.fromJson(jsonString, UserProfile.class);
        this.userProfileLiveData.setValue(userProfile);
    }

    /**
     * TODO 1.12 isi logic untuk Method addToCart()
     * Method ini digunakan ketika mau menambahkan game diawal jadi saat milih game dari
     * StoreFragment buat dimasukin ke CartFragment
     *
     * @param game
     * @HINT: gunakan method cariGame() untuk cek apakah game yang dipilih dari storeFragment
     * sudah ada didalam Cart.
     */
    public void addToCart(Game game) {
        if (cariGame(game.getId())==null) {
            List<PurchasedGame> tempBelanja = keranjangBelanja.getValue();
            PurchasedGame gameBaru = new PurchasedGame(game, 1);
            tempBelanja.add(gameBaru);
            keranjangBelanja.setValue(tempBelanja);
        } else {
            List<PurchasedGame> belanjaan = keranjangBelanja.getValue();
            for (PurchasedGame purchasedGame : belanjaan) {
                if (purchasedGame.getGame().getId() == game.getId()) {
                    purchasedGame.setJumlah(purchasedGame.getJumlah()+1);
                    return;
                }
            }
            keranjangBelanja.setValue(belanjaan);
        }
    }

    /**
     * TODO BONUS 2.0 isi logic untuk method clearCart
     * Method ini digunakan untuk clearing cart setelah pdf digeneraete
     */
    public void clearCart() {
        List<PurchasedGame> temp = keranjangBelanja.getValue();
        temp.removeAll(temp);
        keranjangBelanja.setValue(temp);
    }

    /**
     * TODO BONUS 2.3 isi logic untuk method addOneFromCart()
     * Method ini digunakan untuk button tambah di cartFragment
     * @param game
     * @HINT: gunakan method cariGame() untuk mendapat referensi game yang dimaksud
     */
    public void addOneFromCart(Game game) {
        List<PurchasedGame> temp = keranjangBelanja.getValue();
        PurchasedGame purchasedGame = cariGame(game.getId());
        int awal = purchasedGame.getJumlah();

        for(PurchasedGame game1 : temp) {
            if(game1 == purchasedGame) {
                game1.setJumlah(awal+1);
                return;
            }
        }
        keranjangBelanja.setValue(temp);
    }

    /**
     * TODO BONUS 2.4 isi logic untuk method removeFromCart()
     * Method ini digunakan untuk button kurang di cartFragment
     * @param game
     */
    public void removeFromCart(Game game) {
        List<PurchasedGame> temp = keranjangBelanja.getValue();
        PurchasedGame purchasedGame = cariGame(game.getId());
        int awal = purchasedGame.getJumlah();

        if(awal>1) {
            for(PurchasedGame game1 : temp) {
                if(game1 == purchasedGame) {
                    game1.setJumlah(awal-1);
                    return;
                }
            }
        } else if(awal == 1) {
            temp.remove(temp.indexOf(purchasedGame));
        }
        keranjangBelanja.setValue(temp);
    }

    private PurchasedGame cariGame(int id) {
        if (keranjangBelanja.getValue() != null) {
            List<PurchasedGame> belanjaan = keranjangBelanja.getValue();
            for (PurchasedGame game : belanjaan) {
                if (game.getGame().getId() == id)
                    return game;
            }
        }
        return null;
    }

    public LiveData<List<Game>> getGameListLiveData() {
        return gameListLiveData;
    }

    public LiveData<List<PurchasedGame>> getKeranjangBelanja() {
        return keranjangBelanja;
    }

    public LiveData<String> getErrMsg() {
        return errMsg;
    }

    public LiveData<UserProfile> getUserProfileLiveData() {
        return userProfileLiveData;
    }
}
