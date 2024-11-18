
package View;
import Model.Categories;
import Model.Customers;
import Model.Order;
import Model.OrderProductDetails;
import Model.ProductDetails;
import Model.Products;
import Model.Size;
import Model.Style;
import Model.Suppliers;
import java.awt.Color;
import static java.awt.Color.black;
import static java.awt.Color.white;
import javax.swing.JFrame;
import Model.Users;
import Model.Users1;
import QR_CODE.QRCode;
import QR_CODE.QRCodeGenerator;
import Service.CategoriesService;
import Service.CustomerService;
import Service.DiscountService;
import Service.Excel.ExcelHoaDon;
import Service.OriginalPriceService;
import Service.PointsConversion_Service;
import Service.ProductDetails_Service;
import Service.QuanLyBanHang;
import Service.QuanLyHoaDon;
import Service.QuanLySanPhamService;
import Service.ThongKeService;
import Service.User1Service;
import static Service.User1Service.generateCode;
import Service.UserService;
import Service.VoucherService;
import Utils.ExcelExporter;
import Utils.QRCodeScanner;
import dao.DatabaseConnectionManager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;




public class View_NhanVien extends javax.swing.JFrame {
    
    //Hà
    private QuanLyHoaDon qlHoaDon =  new QuanLyHoaDon();
    private QuanLyBanHang ql = new QuanLyBanHang();
    private int currentUserId;
    private String currentOrderCode; 
    private QRCode qrCode;
    
    //Hà
    private ExcelHoaDon ex = new ExcelHoaDon();
    
    
    //Tuấn
    private int usercode;
    int index;
    //doi mk
    private boolean trangThaiMk = false;
    CategoriesService ctsv = new CategoriesService();
    User1Service us1sv = new User1Service();
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel modelUs = new DefaultTableModel();
    ArrayList<Categories> list = new CategoriesService().getAllCt();
    ArrayList<Users1> listNv = new User1Service().getAllUser1();
    // end Tuấn
    
    
    //Đông 
    int addVoucher = 0;
    int updateVoucher = 0;
    int addDis = 0;
    int updateDis = 0;
     int selectedTabIndex = 0;
    
    // Hòa
    //Product
    private QuanLySanPhamService qlsp = new QuanLySanPhamService();
    private ExcelExporter ec = new ExcelExporter();

    private String selectedCategory = "";
    private String selectedSupplier = "";
    private String selectedStatus = "";
    private String selectedCategorys = "";
    private String selectedStyle = "";
    private String selectedSize = "";
    private String selectedColor = "";
    private String selectedQuantity = "";
    private Double[] selectedPrice = null;

    private String image;
    private QRCodeScanner qrCodeScanner;

    private boolean choice = false;
    private boolean isAddOrUpdate = false;

    private int currentPage = 1;
    private int totalPages = 0;

    private int currentPages = 1;
    private int totalPage = 0;

    private int rowsPerPage = 14;
    private boolean fillter = false;
    //End product
    
    //Hòa Customers
    private CustomerService qlkh = new CustomerService();
    //End
    
    //Phong
    ThongKeService thongKeService = new ThongKeService();
    
    
    public View_NhanVien(int userId) throws SQLDataException, SQLException {
        this.list = new CategoriesService().getAllCt();
        initComponents(); 
        setLocationRelativeTo(null);
        qrCode = new QRCode(camera,anhQRcode, ql, tblDSSanPham);    
        camera.setPreferredSize(new Dimension(153, 106));
         cbbLocTrangThaiHoaDon.addItem("");
         cbbLocTrangThaiHoaDon.addItem("Đã thanh toán");
         cbbLocTrangThaiHoaDon.addItem("Chưa thanh toán");
                      
        this.jpViewQLHD.setVisible(false);
        this.jpViewQLDM.setVisible(false);
        this.jpViewQLKH.setVisible(false);
        this.jpViewQLKM.setVisible(false);      
        this.jpViewQLTK.setVisible(false);
        this.jpViewTK.setVisible(false);
        this.jpViewQLBH2.setVisible(true);      
        this.jpViewDoiMK.setVisible(false);
        this.jpViewQLSPP.setVisible(false);
        this.jpViewQLSPP2.setVisible(false);
        this.jpViewQLSPP3.setVisible(false);
        this.jpQLBH.setBackground(new Color(220, 220, 220));
        
        
        
        
        //Của Hà
        this.currentUserId = userId;      
        String tenNhanVien = getEmployeeNameById(this.currentUserId);
        lblNhanVienBan.setText(tenNhanVien != null ? tenNhanVien : "Không tìm thấy họ tên nhân viên");                   
        tblDSHD.setModel(qlHoaDon.loadOrderData());
         

        ql.LoadDSSPTrongQLBH(tblDSSanPham);
        ql.loadVoucher(cbbVouchers);
//        ql.loadKhachHang(tblKhachH);
        ql.DanhSachHoaDonCho(tblHoaDonCho);
        
        // cập nhật giá voucher khi chọn
        cbbVouchers.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbbVouchersActionPerformed(evt);}});
        
        // cập nhật bảng spct trong hóa đơn nếu k có dòng được chọn
        tblDSHD.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
        tblDSHDFocusLost(evt);}});
        
        
    // Của Tuấn
    //danh mục
        model = (DefaultTableModel) tblDanhSachDM.getModel();
        ctsv.filltoTable(tblDanhSachDM);
//        ctsv.showDetails(model, tblDanhSachDM, index, txtMaDM, txtTenDM);

        // end Tuấn
        
        
        
        //Đông 
           TabGiamGia.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Lấy chỉ số của tab hiện tại
                selectedTabIndex = TabGiamGia.getSelectedIndex();

            }
        });
    // end Dong
                
        //Product
        this.init();
        this.addCbbListener();
        currentPage = 1;
        updatePagination(qlsp.getAllProductDetail());

        currentPages = 1;
        updatePaginations(qlsp.getAllProduct());
        //Customers
        this.qlkh.loadTableCus(this.tblKhachH, this.qlkh.getAllCustomer());
        this.setEnableFormKH();
        
        
        //Phong
       thongKeService.LoadTongDoanhSo(lblDoanhThuTong);
        thongKeService.LocDSTheoThang(jDTTNam, jDTTThang, lblDoanhThuThang);
        thongKeService.LoadTableThongKe(tblDanhSachSPdaBan);
        thongKeService.LocDoanhSoTheoNgay(jDateDoanhThuNgay, lblDoanhThuNgay);
        thongKeService.loadTongSoLuong(lblTongSP);
        thongKeService.loadTongSoLuongDaBan(lblSPDaBan);
       
    }
    
    
    //            ----------------------Ngoai Ham Khoi Tao---------------------
    
    
    //Tuấn
    
     public Users1 getUsers() {
        Users1 us = new Users1();
        us.setHoTen(txtHoTen.getText().trim());
        us.setUsername(txtUsername.getText().trim());
        us.setEmail(txtEmail.getText().trim());
        us.setPhone(txtSdt.getText().trim());
        us.setAddress(txtDiaChi.getText().trim());
        us.setRoleId(Boolean.valueOf(txtVaiTro.getText().trim()));
        return us;
    }
    
    //code danh mục 
    private void filltoTable() throws SQLDataException {
        ctsv.filltoTable(tblDanhSachDM);
    }

    private void showDetails() throws SQLDataException {
        ctsv.showDetails(model, tblDanhSachDM, index, txtMaDM, txtTenDM);
    }

    private Categories readForm() {
        return ctsv.readForm(txtMaDM, txtTenDM);
    }

    private boolean check() {
        String tenDM = txtTenDM.getText();
        return ctsv.check(tenDM);
    }

    //nhanvien 
    
    






    
    
    //Hòa
    //PRODUCT
    private void init() {

        this.qlsp.addCbbDanhMuc(this.cbbDanhMuc);
        this.qlsp.addCbbDanhMuc(this.cbbLocTheoDM);
        this.qlsp.addCbbDanhMuc(this.cbbLoaiSPCT);
        this.qlsp.addCbbNhaCC(this.cbbNhaCC);
        this.qlsp.addCbbNhaCC(this.cbbLocNhaCC);
        this.qlsp.addCbbStyle(this.cbbStyle);
        this.qlsp.addCbbStyle(this.cbbStyles);
        this.qlsp.addCbbColor(this.cbbColor);
        this.qlsp.addCbbColor(this.cbbMauSacSPCT);
        this.qlsp.addCbbSize(this.cbbSizes);
        this.qlsp.addCbbSize(this.cbbKichThuocSPCT);
        this.qlsp.addCbbLocSoLuong(this.cbbSoLuong);
        this.qlsp.addCbbLocGiaBan(this.cbbGiaBan);
        this.qlsp.addCbbLocTrangThai(this.cbbTrangThaiSP);

        this.qlsp.loadTable(this.tblHienThiKetQua, qlsp.getAllProduct(), currentPages, rowsPerPage);
        this.qlsp.loadTableDetail(this.tblSPCT, this.qlsp.getAllProductDetail(), currentPage, rowsPerPage);
        this.qlsp.loadTableColor(tblMauSac, this.qlsp.getAllColor());
        this.qlsp.loadTableSize(tblKichThuoc, this.qlsp.getAllSize());
        this.qlsp.loadTableSupplier(tblNhaCungCap, this.qlsp.getAllSuppliers());
        this.qlsp.loadTableStyle(tblPhongCach, this.qlsp.getAllStyle());

        this.qrCodeScanner = new QRCodeScanner(this.pnWebCam, this.qlsp, this.tblSPCT);

    }

    private void displayImage(String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        int labelWidth = lblImage.getWidth();
        int labelHeight = lblImage.getHeight();

        if (icon.getIconWidth() > labelWidth || icon.getIconHeight() > labelHeight) {
            Image image = icon.getImage().getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);

            icon = new ImageIcon(image);
        }

        lblImage.setIcon(icon);
    }

    private void addCbbListener() throws SQLException {

        this.setEnableFormSP();
        this.setEnableFormSPCT();
        this.setEnableFormSize(false);
        this.setEnableFormStyle(false);
        this.setEnableFormColor(false);
        this.setEnableFormSupp(false);

        this.cbbLocTheoDM.addActionListener(e -> {
            selectedCategory = (String) cbbLocTheoDM.getSelectedItem();
            filterAndLoadTable();
        });

        this.cbbLocNhaCC.addActionListener(e -> {
            selectedSupplier = (String) this.cbbLocNhaCC.getSelectedItem();
            filterAndLoadTable();
        });

        this.cbbTrangThaiSP.addActionListener(e -> {
            selectedStatus = (String) this.cbbTrangThaiSP.getSelectedItem();

            filterAndLoadTable();
        });

        this.cbbLoaiSPCT.addActionListener(e -> {
            selectedCategorys = (String) this.cbbLoaiSPCT.getSelectedItem();
            filterAndLoadTables();
        });

        this.cbbStyles.addActionListener(e -> {
            selectedStyle = (String) this.cbbStyles.getSelectedItem();

            filterAndLoadTables();
        });

        this.cbbKichThuocSPCT.addActionListener(e -> {
            selectedSize = (String) this.cbbKichThuocSPCT.getSelectedItem();
            filterAndLoadTables();
        });
        this.cbbMauSacSPCT.addActionListener(e -> {
            selectedColor = (String) this.cbbMauSacSPCT.getSelectedItem();

            filterAndLoadTables();
        });
        this.cbbSoLuong.addActionListener(e -> {
            selectedQuantity = (String) this.cbbSoLuong.getSelectedItem();

            filterAndLoadTables();
        });
        this.cbbGiaBan.addActionListener(e -> {
            String selectedPriceString = (String) this.cbbGiaBan.getSelectedItem();
            selectedPrice = qlsp.convertPrice(selectedPriceString);
            filterAndLoadTables();
        });
        
        this.tbpThuocTinh.addChangeListener(e -> {
            this.choice = false;
            this.isAddOrUpdate = false;
            this.setEnableFormColor(false);
            this.setEnableFormSize(false);
            this.setEnableFormStyle(false);
            this.setEnableFormSupp(false);
            this.tblMauSac.clearSelection();
            this.tblKichThuoc.clearSelection();
            this.tblNhaCungCap.clearSelection();
            this.tblPhongCach.clearSelection();
            this.setInputFields("màu sắc");
            this.setInputFields("kích thước");
            this.setInputFields("nhà cung cấp");
            this.setInputFields("phong cách");
        });

    }

    public Boolean isConfirmMessage(String message) {
        int showMessage = JOptionPane.showConfirmDialog(this, message, "Xác nhận", JOptionPane.YES_NO_OPTION);
        return showMessage == JOptionPane.YES_OPTION;
    }

    public void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean isShowMessageSelected(JTable table) {
        int rowIndex = table.getSelectedRow();
        if (rowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng trên table để tiếp tục cập nhật.");
            return false;
        }
        return true;
    }

    private void setInputFields(String objType) {
        switch (objType) {
            case "màu sắc":
                this.txtMaMau.setText("");
                this.txtTenMau.setText("");
                break;
            case "kích thước":
                this.txtMaKT.setText("");
                this.txtTenKichThuoc.setText("");
                this.txtMoTaSize.setText("");
                break;
            case "nhà cung cấp":
                this.txtNCC.setText("");
                this.txtTenNCC.setText("");
                this.txtSDTNCC.setText("");
                this.txtEmailNCC.setText("");
                this.txtDiaChiNCC.setText("");
                break;
            case "phong cách":
                this.txtMaPC.setText("");
                this.txtTenPhongCach.setText("");
                this.txtMoTaPC.setText("");
                break;
            case "sản phẩm chi tiết":
                this.txtMaSPCT1.setText("");
                this.cbbStyle.setSelectedItem("");
                this.txtMaSP.setText("");
                this.cbbColor.setSelectedItem("");
                this.cbbSizes.setSelectedItem("");
                this.txtQuantity.setText("");
                this.txtPrice.setText("");
                this.lblImage.setIcon(null);
                this.lblImage.setText("Click để chọn ảnh!");
                break;
            case "sản phẩm":
                this.txtMaSanPham.setText("");
                this.txtTenSP.setText("");
                this.txtMotaSPP.setText("");
                this.cbbDanhMuc.setSelectedItem("");
                this.cbbNhaCC.setSelectedItem("");
                this.rdoRun.setSelected(true);
                break;
        }
    }

    private void handleAddOrUpdate(String id, Object obj, String objType, Runnable addOrUpdateFunc) {
        if (this.isConfirmMessage("Bạn có chắc chắn muốn " + (this.choice ? "thêm mới" : "cập nhật") + " " + objType + " " + id + " không?")) {
            addOrUpdateFunc.run();
            this.showMessage((this.choice ? "Thêm mới" : "Cập nhật") + " " + objType + " " + id + " thành công", "Thành công");
            setInputFields(objType);
            this.image = null;
        } else {
            setInputFields(objType);
            this.image = null;
        }
    }

    private void filterAndLoadTable() {

        java.util.List<Products> filteredProducts = this.qlsp.filterProducts(selectedCategory, selectedSupplier, selectedStatus);
        this.qlsp.loadTable(tblHienThiKetQua, filteredProducts, currentPages, rowsPerPage);
        currentPages = 1;
        updatePaginations(filteredProducts);
    }

    private void filterAndLoadTables() {
        java.util.List<ProductDetails> filteredProductDetails = qlsp.filterProductDetail(
                selectedCategorys, selectedStyle, selectedSize, selectedColor,
                qlsp.convertQuantity(selectedQuantity), selectedPrice);
        this.qlsp.loadTableDetail(tblSPCT, filteredProductDetails, currentPage, rowsPerPage);
        currentPage = 1;
        updatePagination(filteredProductDetails);
    }

    private void setEnableFormSPCT() {
        this.txtMaSPCT1.setEnabled(false);
        this.cbbStyle.setEnabled(false);
        this.txtMaSP.setEnabled(false);
        this.cbbColor.setEnabled(false);
        this.cbbSizes.setEnabled(false);
        this.txtQuantity.setEnabled(false);
        this.txtPrice.setEnabled(false);
    }

    private void setEnableFormSP() {
        this.txtMaSanPham.setEnabled(false);
        this.txtTenSP.setEnabled(false);
        this.txtMotaSPP.setEnabled(false);
        this.cbbDanhMuc.setEnabled(false);
        this.cbbNhaCC.setEnabled(false);
        this.rdoRun.setEnabled(false);
        this.rdoStop.setEnabled(false);
    }

    private void setEnableFormSize(Boolean stt) {
        this.txtMaKT.setEnabled(stt);
        this.txtTenKichThuoc.setEnabled(stt);
        this.txtMoTaSize.setEnabled(stt);
    }

    private void setEnableFormStyle(Boolean stt) {
        this.txtMaPC.setEnabled(stt);
        this.txtTenPhongCach.setEnabled(stt);
        this.txtMoTaPC.setEnabled(stt);
    }

    private void setEnableFormColor(Boolean stt) {
        this.txtMaMau.setEnabled(stt);
        this.txtTenMau.setEnabled(stt);
    }

    private void setEnableFormSupp(Boolean stt) {
        this.txtNCC.setEnabled(stt);
        this.txtTenNCC.setEnabled(stt);
        this.txtSDTNCC.setEnabled(stt);
        this.txtEmailNCC.setEnabled(stt);
        this.txtDiaChiNCC.setEnabled(stt);
    }

    private void updatePagination(java.util.List<ProductDetails> allProductDetail) {
        int totalRows = allProductDetail.size();
        totalPages = (int) Math.ceil((double) totalRows / rowsPerPage);
        if (totalPages == 0) {
            totalPages = 1;
        }
        updateTable(allProductDetail);
        lblPage.setText(currentPage + " / " + totalPages);
    }

    private void updateTable(java.util.List<ProductDetails> allProductDetatil) {
        int startRow = (currentPage - 1) * rowsPerPage;
        int endRow = Math.min(startRow + rowsPerPage, allProductDetatil.size());
        java.util.List<ProductDetails> pageProductDetail = allProductDetatil.subList(startRow, endRow);
        qlsp.loadTableDetail(tblSPCT, pageProductDetail, currentPage, rowsPerPage);
    }

    private void updatePaginations(java.util.List<Products> allProduct) {
        int totalRows = allProduct.size();
        totalPage = (int) Math.ceil((double) totalRows / rowsPerPage);
        if (totalPage == 0) {
            totalPage = 1;
        }
        updateTables(allProduct);
        lblPages.setText(currentPages + " / " + totalPage);
    }

    private void updateTables(java.util.List<Products> allProducts) {
        int startRow = (currentPages - 1) * rowsPerPage;
        int endRow = Math.min(startRow + rowsPerPage, allProducts.size());
        java.util.List<Products> pageProduct = allProducts.subList(startRow, endRow);
        qlsp.loadTable(tblHienThiKetQua, pageProduct, currentPages, rowsPerPage);
    }

    private void navigatePage(int targetPage, boolean isFiltered) {
        if (targetPage < 1 || targetPage > totalPages) {
            return;
        }
        currentPage = targetPage;
        if (!isFiltered) {
            updatePagination(qlsp.filterProductDetail(
                    selectedCategorys, selectedStyle, selectedSize, selectedColor,
                    qlsp.convertQuantity(selectedQuantity), selectedPrice));
        } else {
            java.util.List<ProductDetails> list = new ArrayList<>();
            int[] selectedRows = this.tblHienThiKetQua.getSelectedRows();
            for (int rowIndex : selectedRows) {
                String productCode = (String) this.tblHienThiKetQua.getValueAt(rowIndex, 1);
                list.addAll(this.qlsp.getListById(productCode));
            }
            updatePagination(list);
        }
    }
    //END PRODUCT
    
    
    //Customers
    private void setEnableFormKH() {
        this.txtITenKhachHang1.setEnabled(false);
        this.txtSoDiienThoai.setEnabled(false);
        this.txtDiemTichLuy.setEnabled(false);
        this.txtTinhDiem.setEnabled(false);
        this.txtDiemDaDung.setEnabled(false);
        this.txtMaKH.setEnabled(false);
    }

    private void setText() {
        this.txtITenKhachHang1.setText("");
        this.txtSoDiienThoai.setText("");
        this.txtDiemTichLuy.setText("");
        this.txtTinhDiem.setText("");
        this.txtDiemDaDung.setText("");
        this.txtMaKH.setText("");
    }
    //End
    

 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jpContainer = new javax.swing.JPanel();
        MenuHeThong = new javax.swing.JPanel();
        jpQLBH = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jpQLSP = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jpQLDM = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jpQLKM = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jpQLKH = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jpTK = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jpQLTK = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        btnDangXuat = new javax.swing.JButton();
        lblLogo = new javax.swing.JLabel();
        jpQLHD = new javax.swing.JPanel();
        jLabel91 = new javax.swing.JLabel();
        ViewHeThong = new javax.swing.JPanel();
        jpViewQLHD = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        BangHoaDon = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDSHD = new javax.swing.JTable();
        jLabel96 = new javax.swing.JLabel();
        txtLocHoaDonTheoMaHD = new javax.swing.JTextField();
        cbbLocTrangThaiHoaDon = new javax.swing.JComboBox<>();
        jLabel146 = new javax.swing.JLabel();
        jLabel147 = new javax.swing.JLabel();
        dateTu = new com.toedter.calendar.JDateChooser();
        dateDen = new com.toedter.calendar.JDateChooser();
        jLabel148 = new javax.swing.JLabel();
        jLabel149 = new javax.swing.JLabel();
        btnXuatExcelHoaDon = new javax.swing.JButton();
        btnLocTheoKhoang = new javax.swing.JButton();
        SanPhamTrongHoaDon = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tblSanPhamTrongHoaDon = new javax.swing.JTable();
        jLabel92 = new javax.swing.JLabel();
        jpViewQLBH2 = new javax.swing.JPanel();
        jp_V_ThemMoiHD = new javax.swing.JPanel();
        jp_V_HoaDon = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblDSSPtrongHD = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        txtTenKhachHang = new javax.swing.JTextField();
        txtSDTKhachHang = new javax.swing.JTextField();
        cbbVouchers = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtSoDiemDaDung = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        lblNhanVienBan = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        lblTongTienThanhToan = new javax.swing.JLabel();
        btnThemKHtuHD = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        lblVAT = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        lblDiemConLai = new javax.swing.JLabel();
        rdoTienMat = new javax.swing.JRadioButton();
        rdoChuyenKhoan = new javax.swing.JRadioButton();
        btnLuuTruHD = new javax.swing.JButton();
        btnThanhToan = new javax.swing.JButton();
        btnXoaKhoiGioHang = new javax.swing.JButton();
        btnCapNhatGioHang = new javax.swing.JButton();
        txtNhapSoLuong1 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        btnKhachLe = new javax.swing.JButton();
        jLabel90 = new javax.swing.JLabel();
        lblTienTruocKhiGiam = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblDSSanPham = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        txtTenSanPham = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        txtNhapSoLuong = new javax.swing.JTextField();
        btnThemSPVaoHD = new javax.swing.JButton();
        jLabel95 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        jLabel144 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtMaSPCT = new javax.swing.JTextField();
        btnLamMoiTTSP = new javax.swing.JButton();
        txtMauSaccc = new javax.swing.JTextField();
        txtLoaiSPpp = new javax.swing.JTextField();
        txtKichThuoccc = new javax.swing.JTextField();
        txtPhongCachhh = new javax.swing.JTextField();
        camera = new javax.swing.JPanel();
        anhQRcode = new javax.swing.JLabel();
        btnQuetMaQRCodee = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblHoaDonCho = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        btnXoaHoaDonCho = new javax.swing.JButton();
        txtMaCTSP = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        btnTiepTucThanhToan = new javax.swing.JButton();
        jpViewQLDM = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDanhSachDM = new javax.swing.JTable();
        jLabel75 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtMaDM = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        txtTenDM = new javax.swing.JTextField();
        jpViewQLTK = new javax.swing.JPanel();
        tabQLNV = new javax.swing.JTabbedPane();
        jpThongTinCN = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtSdt = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtDiaChi = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtVaiTro = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnCapNhatThongTinCN = new javax.swing.JButton();
        btnDoiMatKhau = new javax.swing.JButton();
        jpViewDoiMK = new javax.swing.JPanel();
        formDoiMK = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        txtUsername1 = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        btnXacNhanDMK = new javax.swing.JButton();
        jLabel61 = new javax.swing.JLabel();
        txtConfirmPassword = new javax.swing.JPasswordField();
        txtNewPassword = new javax.swing.JPasswordField();
        txtPassword = new javax.swing.JPasswordField();
        btnQuaylai = new javax.swing.JButton();
        btnEyeMK = new javax.swing.JButton();
        btnEyeMKMoi = new javax.swing.JButton();
        btnEyeXacNhan = new javax.swing.JButton();
        jpViewQLSPP = new javax.swing.JPanel();
        jLabel56 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel161 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel162 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        jLabel163 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtTenSP = new javax.swing.JTextField();
        txtMaSanPham = new javax.swing.JTextField();
        rdoRun = new javax.swing.JRadioButton();
        rdoStop = new javax.swing.JRadioButton();
        jLabel109 = new javax.swing.JLabel();
        cbbDanhMuc = new javax.swing.JComboBox<>();
        cbbNhaCC = new javax.swing.JComboBox<>();
        jScrollPane15 = new javax.swing.JScrollPane();
        txtMotaSPP = new javax.swing.JTextArea();
        btnAddProductDetails = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tblHienThiKetQua = new javax.swing.JTable();
        cbbLocTheoDM = new javax.swing.JComboBox<>();
        jLabel164 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        txtTimKiemTheoMa = new javax.swing.JTextField();
        jLabel165 = new javax.swing.JLabel();
        cbbTrangThaiSP = new javax.swing.JComboBox<>();
        jLabel110 = new javax.swing.JLabel();
        btnHoanTac1 = new javax.swing.JButton();
        cbbLocNhaCC = new javax.swing.JComboBox<>();
        btnExcel = new javax.swing.JButton();
        btnPreviousAll = new javax.swing.JButton();
        btnPreviou = new javax.swing.JButton();
        btnNextt = new javax.swing.JButton();
        btnNexttALL = new javax.swing.JButton();
        lblPages = new javax.swing.JLabel();
        jpViewQLSPP2 = new javax.swing.JPanel();
        jLabel111 = new javax.swing.JLabel();
        btnBackProducts = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        jLabel93 = new javax.swing.JLabel();
        txtMaSPCT1 = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        cbbColor = new javax.swing.JComboBox<>();
        jLabel119 = new javax.swing.JLabel();
        cbbSizes = new javax.swing.JComboBox<>();
        jLabel120 = new javax.swing.JLabel();
        cbbStyle = new javax.swing.JComboBox<>();
        jLabel121 = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        jLabel122 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        btnThemMoiThuocTinh = new javax.swing.JButton();
        lblImage = new javax.swing.JLabel();
        pnWebCam = new javax.swing.JLabel();
        txtMaSP = new javax.swing.JTextField();
        jPanel31 = new javax.swing.JPanel();
        jLabel112 = new javax.swing.JLabel();
        txtSearhProductDetail = new javax.swing.JTextField();
        cbbMauSacSPCT = new javax.swing.JComboBox<>();
        jLabel116 = new javax.swing.JLabel();
        jLabel114 = new javax.swing.JLabel();
        jLabel115 = new javax.swing.JLabel();
        cbbKichThuocSPCT = new javax.swing.JComboBox<>();
        cbbLoaiSPCT = new javax.swing.JComboBox<>();
        jLabel117 = new javax.swing.JLabel();
        cbbStyles = new javax.swing.JComboBox<>();
        btnXemDSSPCT = new javax.swing.JButton();
        btnQRcode = new javax.swing.JButton();
        jScrollPane16 = new javax.swing.JScrollPane();
        tblSPCT = new javax.swing.JTable();
        jLabel168 = new javax.swing.JLabel();
        jLabel169 = new javax.swing.JLabel();
        cbbSoLuong = new javax.swing.JComboBox<>();
        cbbGiaBan = new javax.swing.JComboBox<>();
        btnExcelProductDetail = new javax.swing.JButton();
        btnDowloadQr = new javax.swing.JButton();
        btnPreviouAll = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnNextAll = new javax.swing.JButton();
        lblPage = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jpViewQLSPP3 = new javax.swing.JPanel();
        jLabel124 = new javax.swing.JLabel();
        tbpThuocTinh = new javax.swing.JTabbedPane();
        jPanel13 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        tblMauSac = new javax.swing.JTable();
        jLabel125 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel126 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        txtMaMau = new javax.swing.JTextField();
        txtTenMau = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        tblKichThuoc = new javax.swing.JTable();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel129 = new javax.swing.JLabel();
        jLabel130 = new javax.swing.JLabel();
        jLabel131 = new javax.swing.JLabel();
        txtMaKT = new javax.swing.JTextField();
        txtTenKichThuoc = new javax.swing.JTextField();
        jLabel166 = new javax.swing.JLabel();
        jScrollPane21 = new javax.swing.JScrollPane();
        txtMoTaSize = new javax.swing.JTextArea();
        jLabel132 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel133 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jLabel134 = new javax.swing.JLabel();
        jLabel135 = new javax.swing.JLabel();
        jLabel136 = new javax.swing.JLabel();
        txtNCC = new javax.swing.JTextField();
        txtTenNCC = new javax.swing.JTextField();
        jLabel137 = new javax.swing.JLabel();
        txtSDTNCC = new javax.swing.JTextField();
        jLabel138 = new javax.swing.JLabel();
        txtEmailNCC = new javax.swing.JTextField();
        jLabel139 = new javax.swing.JLabel();
        txtDiaChiNCC = new javax.swing.JTextField();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblNhaCungCap = new javax.swing.JTable();
        jPanel20 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        tblPhongCach = new javax.swing.JTable();
        jPanel32 = new javax.swing.JPanel();
        jLabel141 = new javax.swing.JLabel();
        jLabel142 = new javax.swing.JLabel();
        jLabel143 = new javax.swing.JLabel();
        txtMaPC = new javax.swing.JTextField();
        txtTenPhongCach = new javax.swing.JTextField();
        jLabel123 = new javax.swing.JLabel();
        jScrollPane22 = new javax.swing.JScrollPane();
        txtMoTaPC = new javax.swing.JTextArea();
        jLabel140 = new javax.swing.JLabel();
        btnBackkkkkk = new javax.swing.JButton();
        jpViewQLKH = new javax.swing.JPanel();
        jLabel170 = new javax.swing.JLabel();
        DanhSachKhachHang1 = new javax.swing.JPanel();
        jScrollPane23 = new javax.swing.JScrollPane();
        tblKhachH = new javax.swing.JTable();
        jLabel171 = new javax.swing.JLabel();
        txtITenKhachHang1 = new javax.swing.JTextField();
        txtSoDiienThoai = new javax.swing.JTextField();
        jLabel172 = new javax.swing.JLabel();
        jLabel173 = new javax.swing.JLabel();
        txtDiemTichLuy = new javax.swing.JTextField();
        jLabel174 = new javax.swing.JLabel();
        txtTinhDiem = new javax.swing.JTextField();
        jLabel175 = new javax.swing.JLabel();
        txtDiemDaDung = new javax.swing.JTextField();
        btnThemMoiKH1 = new javax.swing.JButton();
        btnLamMoiKH1 = new javax.swing.JButton();
        btnSuaKhachHang1 = new javax.swing.JButton();
        jLabel176 = new javax.swing.JLabel();
        txtMaKH = new javax.swing.JTextField();
        jpViewTK = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        ThongKeSP = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        txtTimKiemSP = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        tongSP = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        lblTongSP = new javax.swing.JLabel();
        spDaBan = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        lblSPDaBan = new javax.swing.JLabel();
        btnTimKiemSP = new javax.swing.JButton();
        jTuNgay = new com.toedter.calendar.JDateChooser();
        jDenNgay = new com.toedter.calendar.JDateChooser();
        btnLocKhoangSanPham = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblDanhSachSPdaBan = new javax.swing.JTable();
        jLabel47 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        lblDoanhThuThang = new javax.swing.JLabel();
        jLabel113 = new javax.swing.JLabel();
        jLabel145 = new javax.swing.JLabel();
        jDTTThang = new com.toedter.calendar.JMonthChooser();
        jDTTNam = new com.toedter.calendar.JYearChooser();
        jPanel7 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        lblDoanhThuNgay = new javax.swing.JLabel();
        jDateDoanhThuNgay = new com.toedter.calendar.JDateChooser();
        jPanel5 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        lblDoanhThuTong = new javax.swing.JLabel();
        TuLocTheoKhoangUpdate = new com.toedter.calendar.JDateChooser();
        DenLocTheoKhoangUpdate = new com.toedter.calendar.JDateChooser();
        btnLocDoanhSoUpdate = new javax.swing.JButton();
        cbbTOP = new javax.swing.JComboBox<>();
        btnLamMoiThongKe = new javax.swing.JButton();
        jpViewQLKM = new javax.swing.JPanel();
        jLabel76 = new javax.swing.JLabel();
        TabGiamGia = new javax.swing.JTabbedPane();
        Coupon = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblCoupon = new javax.swing.JTable();
        btnAddCou = new javax.swing.JButton();
        btnEditCou = new javax.swing.JButton();
        jLabel98 = new javax.swing.JLabel();
        txtMaGiamGia = new javax.swing.JTextField();
        jLabel99 = new javax.swing.JLabel();
        txtCode = new javax.swing.JTextField();
        jLabel100 = new javax.swing.JLabel();
        txtPhanTramChietKhau = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();
        jLabel102 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        rdoDangKM = new javax.swing.JRadioButton();
        rdoDungKM = new javax.swing.JRadioButton();
        btnVoHH = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        txt_tien_toi_da = new javax.swing.JTextField();
        txt_tien_ap_dung = new javax.swing.JTextField();
        jLabel107 = new javax.swing.JLabel();
        jLabel167 = new javax.swing.JLabel();
        jLabel177 = new javax.swing.JLabel();
        txt_so_luong_vou = new javax.swing.JTextField();
        jdc_ngay_bat_dau_voucher = new com.toedter.calendar.JDateChooser();
        jdc_ngay_ket_thuc_voucher = new com.toedter.calendar.JDateChooser();
        jLabel178 = new javax.swing.JLabel();
        capNhatTichDiem = new javax.swing.JPanel();
        jLabel77 = new javax.swing.JLabel();
        txtMoney = new javax.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        txtPoint = new javax.swing.JTextField();
        jLabel80 = new javax.swing.JLabel();
        txtPoint1 = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        txtMoney1 = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        btnLuuThayDoi = new javax.swing.JButton();
        btnResetPoint = new javax.swing.JButton();
        jLabel84 = new javax.swing.JLabel();
        jLabel179 = new javax.swing.JLabel();
        Discount = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblDiscount = new javax.swing.JTable();
        btnAddDis = new javax.swing.JButton();
        btnEditDis = new javax.swing.JButton();
        jLabel105 = new javax.swing.JLabel();
        jLabel106 = new javax.swing.JLabel();
        txtMa_chiet_khau = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        btnLamMoi2 = new javax.swing.JButton();
        jLabel180 = new javax.swing.JLabel();
        txt_so_tien_giam = new javax.swing.JTextField();
        jdc_ngay_bat_dau = new com.toedter.calendar.JDateChooser();
        jdc_ngay_ket_thuc = new com.toedter.calendar.JDateChooser();
        jScrollPane24 = new javax.swing.JScrollPane();
        tbl_san_pham_khuyen_mai = new javax.swing.JTable();
        txt_tim_sp = new javax.swing.JTextField();
        jLabel181 = new javax.swing.JLabel();
        txt_tim_chuong_trình = new javax.swing.JTextField();
        jLabel182 = new javax.swing.JLabel();
        jLabel183 = new javax.swing.JLabel();
        txt_code_chi_tiet = new javax.swing.JTextField();
        jLabel184 = new javax.swing.JLabel();
        jLabel151 = new javax.swing.JLabel();
        jLabel152 = new javax.swing.JLabel();
        txtMau = new javax.swing.JTextField();
        txtKieuDang = new javax.swing.JTextField();
        txtKichThuoc = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jpContainer.setBackground(new java.awt.Color(255, 255, 255));
        jpContainer.setPreferredSize(new java.awt.Dimension(1250, 600));
        jpContainer.setRequestFocusEnabled(false);

        MenuHeThong.setBackground(new java.awt.Color(255, 255, 255));
        MenuHeThong.setForeground(new java.awt.Color(255, 255, 255));

        jpQLBH.setBackground(new java.awt.Color(255, 102, 0));
        jpQLBH.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpQLBH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpQLBHMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Bán hàng");

        javax.swing.GroupLayout jpQLBHLayout = new javax.swing.GroupLayout(jpQLBH);
        jpQLBH.setLayout(jpQLBHLayout);
        jpQLBHLayout.setHorizontalGroup(
            jpQLBHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpQLBHLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
        jpQLBHLayout.setVerticalGroup(
            jpQLBHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpQLBHLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpQLSP.setBackground(new java.awt.Color(255, 102, 0));
        jpQLSP.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpQLSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpQLSPMouseClicked(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Sản phẩm");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jpQLSPLayout = new javax.swing.GroupLayout(jpQLSP);
        jpQLSP.setLayout(jpQLSPLayout);
        jpQLSPLayout.setHorizontalGroup(
            jpQLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpQLSPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(57, 57, 57))
        );
        jpQLSPLayout.setVerticalGroup(
            jpQLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        jpQLDM.setBackground(new java.awt.Color(255, 102, 0));
        jpQLDM.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpQLDM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpQLDMMouseClicked(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Danh mục");

        javax.swing.GroupLayout jpQLDMLayout = new javax.swing.GroupLayout(jpQLDM);
        jpQLDM.setLayout(jpQLDMLayout);
        jpQLDMLayout.setHorizontalGroup(
            jpQLDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpQLDMLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jpQLDMLayout.setVerticalGroup(
            jpQLDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpQLKM.setBackground(new java.awt.Color(255, 102, 0));
        jpQLKM.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpQLKM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpQLKMMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Khuyến mãi");

        javax.swing.GroupLayout jpQLKMLayout = new javax.swing.GroupLayout(jpQLKM);
        jpQLKM.setLayout(jpQLKMLayout);
        jpQLKMLayout.setHorizontalGroup(
            jpQLKMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpQLKMLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(47, 47, 47))
        );
        jpQLKMLayout.setVerticalGroup(
            jpQLKMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpQLKH.setBackground(new java.awt.Color(255, 102, 0));
        jpQLKH.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpQLKH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpQLKHMouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Khách hàng");

        javax.swing.GroupLayout jpQLKHLayout = new javax.swing.GroupLayout(jpQLKH);
        jpQLKH.setLayout(jpQLKHLayout);
        jpQLKHLayout.setHorizontalGroup(
            jpQLKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpQLKHLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jpQLKHLayout.setVerticalGroup(
            jpQLKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );

        jpTK.setBackground(new java.awt.Color(255, 102, 0));
        jpTK.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpTK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpTKMouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Thống kê");

        javax.swing.GroupLayout jpTKLayout = new javax.swing.GroupLayout(jpTK);
        jpTK.setLayout(jpTKLayout);
        jpTKLayout.setHorizontalGroup(
            jpTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpTKLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpTKLayout.setVerticalGroup(
            jpTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );

        jpQLTK.setBackground(new java.awt.Color(255, 102, 0));
        jpQLTK.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpQLTK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpQLTKMouseClicked(evt);
            }
        });

        jLabel7.setBackground(new java.awt.Color(76, 252, 57));
        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Tài khoản");

        javax.swing.GroupLayout jpQLTKLayout = new javax.swing.GroupLayout(jpQLTK);
        jpQLTK.setLayout(jpQLTKLayout);
        jpQLTKLayout.setHorizontalGroup(
            jpQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpQLTKLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpQLTKLayout.setVerticalGroup(
            jpQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );

        btnDangXuat.setBackground(new java.awt.Color(255, 0, 0));
        btnDangXuat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDangXuat.setForeground(new java.awt.Color(255, 255, 255));
        btnDangXuat.setText("Đăng xuất");
        btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangXuatActionPerformed(evt);
            }
        });

        lblLogo.setBackground(new java.awt.Color(255, 255, 255));
        lblLogo.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\logo.jpg")); // NOI18N

        jpQLHD.setBackground(new java.awt.Color(255, 102, 0));
        jpQLHD.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpQLHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpQLHDMouseClicked(evt);
            }
        });

        jLabel91.setBackground(new java.awt.Color(255, 255, 255));
        jLabel91.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel91.setText("Hóa đơn");

        javax.swing.GroupLayout jpQLHDLayout = new javax.swing.GroupLayout(jpQLHD);
        jpQLHD.setLayout(jpQLHDLayout);
        jpQLHDLayout.setHorizontalGroup(
            jpQLHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpQLHDLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel91)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpQLHDLayout.setVerticalGroup(
            jpQLHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpQLHDLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel91, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout MenuHeThongLayout = new javax.swing.GroupLayout(MenuHeThong);
        MenuHeThong.setLayout(MenuHeThongLayout);
        MenuHeThongLayout.setHorizontalGroup(
            MenuHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpQLBH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpQLSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpQLDM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpQLKM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpQLKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpTK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpQLTK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpQLHD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuHeThongLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDangXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
            .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        MenuHeThongLayout.setVerticalGroup(
            MenuHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuHeThongLayout.createSequentialGroup()
                .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpQLBH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpQLHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpQLSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpQLDM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpQLKM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpQLKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpQLTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(btnDangXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );

        ViewHeThong.setPreferredSize(new java.awt.Dimension(948, 600));

        jpViewQLHD.setBackground(new java.awt.Color(255, 255, 255));
        jpViewQLHD.setPreferredSize(new java.awt.Dimension(948, 600));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 51, 255));
        jLabel20.setText("DANH SÁCH HÓA ĐƠN");

        BangHoaDon.setBackground(new java.awt.Color(255, 255, 255));

        tblDSHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã HĐ", "Mã NV", "Tên NV", "Mã KH", "Ngày tạo", "Mã giảm giá", "VAT", "Phương thức thanh toán", "Tổng giá", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDSHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDSHDMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblDSHD);

        jLabel96.setText("Mã Hóa đơn");

        txtLocHoaDonTheoMaHD.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        txtLocHoaDonTheoMaHD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLocHoaDonTheoMaHDKeyReleased(evt);
            }
        });

        cbbLocTrangThaiHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbLocTrangThaiHoaDonActionPerformed(evt);
            }
        });

        jLabel146.setText("Trạng thái");

        jLabel147.setText("Lọc theo khoảng :");

        dateTu.setDateFormatString("yyyy-MM-dd");

        dateDen.setDateFormatString("yyyy-MM-dd");

        jLabel148.setText("Đến :");

        jLabel149.setText("Từ :");

        btnXuatExcelHoaDon.setBackground(new java.awt.Color(51, 102, 255));
        btnXuatExcelHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXuatExcelHoaDon.setForeground(new java.awt.Color(255, 255, 255));
        btnXuatExcelHoaDon.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\xuatEX.png")); // NOI18N
        btnXuatExcelHoaDon.setText("Xuất Excel");
        btnXuatExcelHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatExcelHoaDonActionPerformed(evt);
            }
        });

        btnLocTheoKhoang.setBackground(new java.awt.Color(51, 102, 255));
        btnLocTheoKhoang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLocTheoKhoang.setForeground(new java.awt.Color(255, 255, 255));
        btnLocTheoKhoang.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\loc.png")); // NOI18N
        btnLocTheoKhoang.setText("Lọc");
        btnLocTheoKhoang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocTheoKhoangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BangHoaDonLayout = new javax.swing.GroupLayout(BangHoaDon);
        BangHoaDon.setLayout(BangHoaDonLayout);
        BangHoaDonLayout.setHorizontalGroup(
            BangHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BangHoaDonLayout.createSequentialGroup()
                .addGroup(BangHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(BangHoaDonLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(BangHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(BangHoaDonLayout.createSequentialGroup()
                                .addComponent(jLabel96)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtLocHoaDonTheoMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jLabel146)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbbLocTrangThaiHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel147)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel149))
                            .addComponent(jLabel148))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BangHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateTu, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateDen, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(BangHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnXuatExcelHoaDon)
                            .addComponent(btnLocTheoKhoang, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(BangHoaDonLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 989, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14))
        );
        BangHoaDonLayout.setVerticalGroup(
            BangHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BangHoaDonLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(BangHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(BangHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dateTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(BangHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel96)
                            .addComponent(txtLocHoaDonTheoMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbLocTrangThaiHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel146)
                            .addComponent(jLabel147)
                            .addComponent(jLabel149)))
                    .addComponent(btnLocTheoKhoang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(BangHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateDen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel148)
                    .addComponent(btnXuatExcelHoaDon))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        SanPhamTrongHoaDon.setBackground(new java.awt.Color(255, 255, 255));

        tblSanPhamTrongHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã SPCT", "Tên SP", "Loại SP", "Màu sắc", "Kích thước", "Phong cách", "Số lượng", "Đơn giá"
            }
        ));
        jScrollPane14.setViewportView(tblSanPhamTrongHoaDon);

        javax.swing.GroupLayout SanPhamTrongHoaDonLayout = new javax.swing.GroupLayout(SanPhamTrongHoaDon);
        SanPhamTrongHoaDon.setLayout(SanPhamTrongHoaDonLayout);
        SanPhamTrongHoaDonLayout.setHorizontalGroup(
            SanPhamTrongHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SanPhamTrongHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane14)
                .addContainerGap())
        );
        SanPhamTrongHoaDonLayout.setVerticalGroup(
            SanPhamTrongHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SanPhamTrongHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel92.setBackground(new java.awt.Color(255, 255, 255));
        jLabel92.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel92.setForeground(new java.awt.Color(51, 51, 255));
        jLabel92.setText("Sản phẩm trong hóa đơn");

        javax.swing.GroupLayout jpViewQLHDLayout = new javax.swing.GroupLayout(jpViewQLHD);
        jpViewQLHD.setLayout(jpViewQLHDLayout);
        jpViewQLHDLayout.setHorizontalGroup(
            jpViewQLHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpViewQLHDLayout.createSequentialGroup()
                .addGap(371, 371, 371)
                .addComponent(jLabel20)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpViewQLHDLayout.createSequentialGroup()
                .addGap(0, 48, Short.MAX_VALUE)
                .addGroup(jpViewQLHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(SanPhamTrongHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BangHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 1002, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpViewQLHDLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel92)))
                .addGap(15, 15, 15))
        );
        jpViewQLHDLayout.setVerticalGroup(
            jpViewQLHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpViewQLHDLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BangHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel92)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SanPhamTrongHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpViewQLBH2.setBackground(new java.awt.Color(255, 255, 255));
        jpViewQLBH2.setOpaque(false);

        jp_V_ThemMoiHD.setBackground(new java.awt.Color(255, 255, 255));

        jp_V_HoaDon.setBackground(new java.awt.Color(255, 255, 255));
        jp_V_HoaDon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51), 2));

        jLabel22.setText("Tên khách hàng:");

        jLabel23.setText("SĐT:");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel24.setText("Giỏ hàng:");

        tblDSSPtrongHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã SPCT", "Tên SP", "Loại SP", "Màu", "Size", "Style", "SL", "Giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDSSPtrongHD.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblDSSPtrongHD.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblDSSPtrongHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDSSPtrongHDMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblDSSPtrongHD);

        jLabel25.setText("Voucher:");

        txtTenKhachHang.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        txtSDTKhachHang.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        txtSDTKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSDTKhachHangActionPerformed(evt);
            }
        });

        cbbVouchers.setBackground(new java.awt.Color(255, 204, 255));

        jLabel26.setText("Phương thức thanh toán:");

        jLabel27.setText("Điểm dùng");

        txtSoDiemDaDung.setText("0");

        jLabel28.setText("Nhân viên giao dịch:");

        lblNhanVienBan.setText("Dương Minh Hà");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel32.setText("Tổng tiền:");

        lblTongTienThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTongTienThanhToan.setText("0 VNĐ");

        btnThemKHtuHD.setBackground(new java.awt.Color(51, 153, 255));
        btnThemKHtuHD.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemKHtuHD.setForeground(new java.awt.Color(255, 255, 255));
        btnThemKHtuHD.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\add.png")); // NOI18N
        btnThemKHtuHD.setText("Thêm KH");
        btnThemKHtuHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemKHtuHDActionPerformed(evt);
            }
        });

        jLabel30.setText("VAT");

        lblVAT.setText("8%");

        jLabel31.setText("Điểm còn");

        lblDiemConLai.setText("0");

        buttonGroup2.add(rdoTienMat);
        rdoTienMat.setSelected(true);
        rdoTienMat.setText("Tiền mặt");

        buttonGroup2.add(rdoChuyenKhoan);
        rdoChuyenKhoan.setText("Chuyển khoản");

        btnLuuTruHD.setBackground(new java.awt.Color(255, 51, 51));
        btnLuuTruHD.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLuuTruHD.setForeground(new java.awt.Color(255, 255, 255));
        btnLuuTruHD.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\luuTru.png")); // NOI18N
        btnLuuTruHD.setText("Lưu trữ HĐ");
        btnLuuTruHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuTruHDActionPerformed(evt);
            }
        });

        btnThanhToan.setBackground(new java.awt.Color(51, 153, 255));
        btnThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThanhToan.setForeground(new java.awt.Color(255, 255, 255));
        btnThanhToan.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\thanhToan.png")); // NOI18N
        btnThanhToan.setText("Thanh Toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        btnXoaKhoiGioHang.setBackground(new java.awt.Color(255, 51, 51));
        btnXoaKhoiGioHang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoaKhoiGioHang.setForeground(new java.awt.Color(255, 255, 255));
        btnXoaKhoiGioHang.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\xoa.png")); // NOI18N
        btnXoaKhoiGioHang.setText("Xóa");
        btnXoaKhoiGioHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaKhoiGioHangActionPerformed(evt);
            }
        });

        btnCapNhatGioHang.setBackground(new java.awt.Color(51, 153, 255));
        btnCapNhatGioHang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCapNhatGioHang.setForeground(new java.awt.Color(255, 255, 255));
        btnCapNhatGioHang.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\sua.png")); // NOI18N
        btnCapNhatGioHang.setText("Cập nhật");
        btnCapNhatGioHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatGioHangActionPerformed(evt);
            }
        });

        txtNhapSoLuong1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        jLabel15.setText("số lượng");

        btnKhachLe.setBackground(new java.awt.Color(51, 153, 255));
        btnKhachLe.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnKhachLe.setForeground(new java.awt.Color(255, 255, 255));
        btnKhachLe.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\Khachle.png")); // NOI18N
        btnKhachLe.setText("Khách lẻ");
        btnKhachLe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhachLeActionPerformed(evt);
            }
        });

        jLabel90.setText("Tiền trước khi giảm:");

        lblTienTruocKhiGiam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTienTruocKhiGiam.setText("0 VNĐ");

        javax.swing.GroupLayout jp_V_HoaDonLayout = new javax.swing.GroupLayout(jp_V_HoaDon);
        jp_V_HoaDon.setLayout(jp_V_HoaDonLayout);
        jp_V_HoaDonLayout.setHorizontalGroup(
            jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_V_HoaDonLayout.createSequentialGroup()
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_V_HoaDonLayout.createSequentialGroup()
                        .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jp_V_HoaDonLayout.createSequentialGroup()
                                    .addGap(45, 45, 45)
                                    .addComponent(jLabel15))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_V_HoaDonLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel23)))
                            .addGroup(jp_V_HoaDonLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel22)))
                        .addGap(33, 33, 33)
                        .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNhapSoLuong1)
                            .addComponent(txtSDTKhachHang)
                            .addComponent(txtTenKhachHang, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(24, 24, 24))
                    .addGroup(jp_V_HoaDonLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCapNhatGioHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXoaKhoiGioHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnKhachLe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemKHtuHD, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(12, 12, 12))
            .addGroup(jp_V_HoaDonLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel26)
                    .addComponent(jLabel30)
                    .addComponent(jLabel25)
                    .addComponent(jLabel31)
                    .addComponent(btnLuuTruHD, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel28)
                    .addComponent(jLabel90))
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_V_HoaDonLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49))
                    .addGroup(jp_V_HoaDonLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jp_V_HoaDonLayout.createSequentialGroup()
                                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cbbVouchers, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jp_V_HoaDonLayout.createSequentialGroup()
                                        .addComponent(rdoTienMat, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(rdoChuyenKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lblDiemConLai, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(37, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_V_HoaDonLayout.createSequentialGroup()
                                .addComponent(lblVAT)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_V_HoaDonLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel27)
                                .addGap(18, 18, 18)
                                .addComponent(txtSoDiemDaDung, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jp_V_HoaDonLayout.createSequentialGroup()
                                .addComponent(lblNhanVienBan)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jp_V_HoaDonLayout.createSequentialGroup()
                                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblTienTruocKhiGiam, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblTongTienThanhToan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );
        jp_V_HoaDonLayout.setVerticalGroup(
            jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_V_HoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKhachLe))
                .addGap(6, 6, 6)
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtSDTKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemKHtuHD))
                .addGap(25, 25, 25)
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel24)
                    .addComponent(btnCapNhatGioHang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtNhapSoLuong1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoaKhoiGioHang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(cbbVouchers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(rdoTienMat)
                    .addComponent(rdoChuyenKhoan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(lblDiemConLai)
                    .addComponent(jLabel27)
                    .addComponent(txtSoDiemDaDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(lblVAT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(lblNhanVienBan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel90)
                    .addComponent(lblTienTruocKhiGiam))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTongTienThanhToan)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jp_V_HoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThanhToan)
                    .addComponent(btnLuuTruHD))
                .addGap(9, 9, 9))
        );

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 0, 255));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("THÊM MỚI HÓA ĐƠN");

        jScrollPane6.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51), 2));

        tblDSSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã SPCT", "Tên SP", "Loại SP", "Màu sắc", "Kích thước", "Phong cách", "Số lượng", "Giá bán"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDSSanPham.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblDSSanPham.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblDSSanPham.setGridColor(new java.awt.Color(255, 255, 153));
        tblDSSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDSSanPhamMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblDSSanPham);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 0), 2));

        jLabel35.setText("Tên sản phẩm");

        txtTenSanPham.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        txtTenSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenSanPhamActionPerformed(evt);
            }
        });

        jLabel36.setText("Nhập số lượng");

        txtNhapSoLuong.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        btnThemSPVaoHD.setBackground(new java.awt.Color(51, 153, 255));
        btnThemSPVaoHD.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemSPVaoHD.setForeground(new java.awt.Color(255, 255, 255));
        btnThemSPVaoHD.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\add1.png")); // NOI18N
        btnThemSPVaoHD.setText("Thêm");
        btnThemSPVaoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSPVaoHDActionPerformed(evt);
            }
        });

        jLabel95.setText("Loại sản phẩm");

        jLabel37.setText("Màu sắc");

        jLabel97.setText("Kích thước");

        jLabel144.setText("Phong cách");

        jLabel29.setText("Mã SPCT");

        txtMaSPCT.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        btnLamMoiTTSP.setBackground(new java.awt.Color(51, 153, 255));
        btnLamMoiTTSP.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLamMoiTTSP.setForeground(new java.awt.Color(255, 255, 255));
        btnLamMoiTTSP.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\reset.png")); // NOI18N
        btnLamMoiTTSP.setMaximumSize(new java.awt.Dimension(115, 31));
        btnLamMoiTTSP.setMinimumSize(new java.awt.Dimension(115, 31));
        btnLamMoiTTSP.setPreferredSize(new java.awt.Dimension(115, 31));
        btnLamMoiTTSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiTTSPActionPerformed(evt);
            }
        });

        txtMauSaccc.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        txtLoaiSPpp.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        txtKichThuoccc.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        txtPhongCachhh.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        camera.setBackground(new java.awt.Color(255, 255, 255));

        anhQRcode.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\QR.png")); // NOI18N

        javax.swing.GroupLayout cameraLayout = new javax.swing.GroupLayout(camera);
        camera.setLayout(cameraLayout);
        cameraLayout.setHorizontalGroup(
            cameraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cameraLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(anhQRcode)
                .addGap(21, 21, 21))
        );
        cameraLayout.setVerticalGroup(
            cameraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(anhQRcode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 106, Short.MAX_VALUE)
        );

        btnQuetMaQRCodee.setBackground(new java.awt.Color(51, 153, 255));
        btnQuetMaQRCodee.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnQuetMaQRCodee.setForeground(new java.awt.Color(255, 255, 255));
        btnQuetMaQRCodee.setText("Quét QR");
        btnQuetMaQRCodee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuetMaQRCodeeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel95)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel29)
                                    .addComponent(jLabel37))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMauSaccc, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                            .addComponent(txtLoaiSPpp)
                            .addComponent(txtMaSPCT)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNhapSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGap(31, 31, 31)
                                    .addComponent(jLabel144))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel97)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel35)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPhongCachhh, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtKichThuoccc, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(camera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnThemSPVaoHD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLamMoiTTSP, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                        .addComponent(btnQuetMaQRCodee, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(txtMaSPCT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35)
                            .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(jLabel97)
                            .addComponent(txtMauSaccc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtKichThuoccc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel95)
                            .addComponent(jLabel144)
                            .addComponent(txtLoaiSPpp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPhongCachhh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(camera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnLamMoiTTSP, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtNhapSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel36)
                                .addComponent(btnThemSPVaoHD, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnQuetMaQRCodee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(51, 51, 255));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("Hóa đơn");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(51, 51, 255));
        jLabel33.setText("Danh sách sản phẩm");

        jScrollPane7.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51), 2));

        tblHoaDonCho.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã HD", "Ngày tạo", "Tên KH", "Số điện thoại", "Mã SPCT", "Số lượng", "Tổng tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDonCho.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblHoaDonCho.setGridColor(new java.awt.Color(255, 255, 255));
        tblHoaDonCho.setShowGrid(false);
        tblHoaDonCho.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonChoMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblHoaDonCho);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 255));
        jLabel11.setText("Danh sách HDC");

        btnXoaHoaDonCho.setBackground(new java.awt.Color(255, 51, 51));
        btnXoaHoaDonCho.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoaHoaDonCho.setForeground(new java.awt.Color(255, 255, 255));
        btnXoaHoaDonCho.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\xoa.png")); // NOI18N
        btnXoaHoaDonCho.setText("Xóa");
        btnXoaHoaDonCho.setPreferredSize(new java.awt.Dimension(80, 31));
        btnXoaHoaDonCho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaHoaDonChoActionPerformed(evt);
            }
        });

        txtMaCTSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaCTSPActionPerformed(evt);
            }
        });
        txtMaCTSP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMaCTSPKeyReleased(evt);
            }
        });

        jLabel39.setText("Mã SPCT");

        btnTiepTucThanhToan.setBackground(new java.awt.Color(51, 153, 255));
        btnTiepTucThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTiepTucThanhToan.setForeground(new java.awt.Color(255, 255, 255));
        btnTiepTucThanhToan.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\thanhtoan1.png")); // NOI18N
        btnTiepTucThanhToan.setText("Tiếp tục");
        btnTiepTucThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTiepTucThanhToanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jp_V_ThemMoiHDLayout = new javax.swing.GroupLayout(jp_V_ThemMoiHD);
        jp_V_ThemMoiHD.setLayout(jp_V_ThemMoiHDLayout);
        jp_V_ThemMoiHDLayout.setHorizontalGroup(
            jp_V_ThemMoiHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_V_ThemMoiHDLayout.createSequentialGroup()
                .addGap(318, 318, 318)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(132, 132, 132)
                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(149, Short.MAX_VALUE))
            .addGroup(jp_V_ThemMoiHDLayout.createSequentialGroup()
                .addGroup(jp_V_ThemMoiHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_V_ThemMoiHDLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jp_V_ThemMoiHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane6)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel33)
                            .addGroup(jp_V_ThemMoiHDLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(59, 59, 59)
                                .addComponent(btnXoaHoaDonCho, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTiepTucThanhToan))))
                    .addGroup(jp_V_ThemMoiHDLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMaCTSP, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jp_V_HoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jp_V_ThemMoiHDLayout.setVerticalGroup(
            jp_V_ThemMoiHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_V_ThemMoiHDLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jp_V_ThemMoiHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_V_ThemMoiHDLayout.createSequentialGroup()
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_V_ThemMoiHDLayout.createSequentialGroup()
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jp_V_ThemMoiHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jp_V_ThemMoiHDLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jp_V_ThemMoiHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaCTSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jp_V_ThemMoiHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(btnTiepTucThanhToan)
                            .addComponent(btnXoaHoaDonCho, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jp_V_HoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpViewQLBH2Layout = new javax.swing.GroupLayout(jpViewQLBH2);
        jpViewQLBH2.setLayout(jpViewQLBH2Layout);
        jpViewQLBH2Layout.setHorizontalGroup(
            jpViewQLBH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1065, Short.MAX_VALUE)
            .addGroup(jpViewQLBH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jp_V_ThemMoiHD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpViewQLBH2Layout.setVerticalGroup(
            jpViewQLBH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 747, Short.MAX_VALUE)
            .addGroup(jpViewQLBH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jp_V_ThemMoiHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpViewQLDM.setBackground(new java.awt.Color(255, 255, 255));
        jpViewQLDM.setPreferredSize(new java.awt.Dimension(1139, 703));

        jLabel73.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(255, 0, 0));
        jLabel73.setText("Thông tin danh mục");

        tblDanhSachDM.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "STT", "Mã danh mục", "Tên danh mục"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDanhSachDM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDanhSachDMMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblDanhSachDM);

        jLabel75.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(255, 0, 0));
        jLabel75.setText("Danh sách danh mục hiện tại:");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Mã danh mục");

        txtMaDM.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        txtMaDM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaDMActionPerformed(evt);
            }
        });

        jLabel74.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel74.setText("Tên danh mục");

        txtTenDM.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        txtTenDM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenDMActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel74))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTenDM, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaDM, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtMaDM, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel74)
                    .addComponent(txtTenDM, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout jpViewQLDMLayout = new javax.swing.GroupLayout(jpViewQLDM);
        jpViewQLDM.setLayout(jpViewQLDMLayout);
        jpViewQLDMLayout.setHorizontalGroup(
            jpViewQLDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpViewQLDMLayout.createSequentialGroup()
                .addGroup(jpViewQLDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpViewQLDMLayout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addGroup(jpViewQLDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel75)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jpViewQLDMLayout.createSequentialGroup()
                        .addGap(348, 348, 348)
                        .addComponent(jLabel73)))
                .addContainerGap(196, Short.MAX_VALUE))
        );
        jpViewQLDMLayout.setVerticalGroup(
            jpViewQLDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpViewQLDMLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel73)
                .addGap(30, 30, 30)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(jLabel75)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );

        jpViewQLTK.setBackground(new java.awt.Color(255, 255, 255));
        jpViewQLTK.setPreferredSize(new java.awt.Dimension(948, 600));

        tabQLNV.setBackground(new java.awt.Color(204, 255, 204));
        tabQLNV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabQLNVMouseClicked(evt);
            }
        });

        jpThongTinCN.setBackground(new java.awt.Color(255, 255, 255));

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setText("Thông tin cá nhân");

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 255));
        jLabel12.setText("Họ và tên");

        txtHoTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHoTenActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 255));
        jLabel13.setText("Username");

        jLabel16.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 255));
        jLabel16.setText("Email");

        jLabel17.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 255));
        jLabel17.setText("Số điện thoại");

        jLabel18.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 255));
        jLabel18.setText("Địa chỉ");

        jLabel19.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 51, 255));
        jLabel19.setText("Chức vụ");

        jPanel1.setBackground(new java.awt.Color(255, 249, 255));

        btnCapNhatThongTinCN.setBackground(new java.awt.Color(255, 255, 51));
        btnCapNhatThongTinCN.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnCapNhatThongTinCN.setForeground(new java.awt.Color(51, 51, 255));
        btnCapNhatThongTinCN.setText("Cập nhật thông tin");
        btnCapNhatThongTinCN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatThongTinCNActionPerformed(evt);
            }
        });

        btnDoiMatKhau.setBackground(new java.awt.Color(255, 255, 51));
        btnDoiMatKhau.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnDoiMatKhau.setForeground(new java.awt.Color(51, 51, 255));
        btnDoiMatKhau.setText("Đổi mật khẩu");
        btnDoiMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoiMatKhauActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCapNhatThongTinCN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDoiMatKhau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(btnCapNhatThongTinCN, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(btnDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpThongTinCNLayout = new javax.swing.GroupLayout(jpThongTinCN);
        jpThongTinCN.setLayout(jpThongTinCNLayout);
        jpThongTinCNLayout.setHorizontalGroup(
            jpThongTinCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpThongTinCNLayout.createSequentialGroup()
                .addGroup(jpThongTinCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpThongTinCNLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jpThongTinCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel17)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpThongTinCNLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jpThongTinCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpThongTinCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpThongTinCNLayout.createSequentialGroup()
                        .addGroup(jpThongTinCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtHoTen)
                            .addComponent(txtUsername)
                            .addComponent(txtEmail)
                            .addComponent(txtSdt)
                            .addComponent(txtDiaChi)
                            .addComponent(txtVaiTro))
                        .addGap(75, 75, 75))
                    .addGroup(jpThongTinCNLayout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 159, Short.MAX_VALUE)))
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpThongTinCNLayout.setVerticalGroup(
            jpThongTinCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpThongTinCNLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jpThongTinCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jpThongTinCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(jpThongTinCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jpThongTinCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(jpThongTinCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addGroup(jpThongTinCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addContainerGap(180, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpThongTinCNLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabQLNV.addTab("Thông tin tài khoản", jpThongTinCN);

        javax.swing.GroupLayout jpViewQLTKLayout = new javax.swing.GroupLayout(jpViewQLTK);
        jpViewQLTK.setLayout(jpViewQLTKLayout);
        jpViewQLTKLayout.setHorizontalGroup(
            jpViewQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabQLNV)
        );
        jpViewQLTKLayout.setVerticalGroup(
            jpViewQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabQLNV)
        );

        jpViewDoiMK.setBackground(new java.awt.Color(255, 255, 255));
        jpViewDoiMK.setPreferredSize(new java.awt.Dimension(948, 600));

        formDoiMK.setBackground(new java.awt.Color(255, 255, 255));
        formDoiMK.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 3));

        jLabel57.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel57.setText("Username");

        jLabel58.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel58.setText("Password");

        jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel59.setText("NewPassword");

        jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel60.setText(" ConfirmPassword");

        btnXacNhanDMK.setBackground(new java.awt.Color(51, 102, 255));
        btnXacNhanDMK.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXacNhanDMK.setForeground(new java.awt.Color(255, 255, 255));
        btnXacNhanDMK.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\luuTru1.png")); // NOI18N
        btnXacNhanDMK.setText("Lưu");
        btnXacNhanDMK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanDMKActionPerformed(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(0, 0, 255));
        jLabel61.setText("Đổi mật khẩu");

        btnQuaylai.setBackground(new java.awt.Color(51, 102, 255));
        btnQuaylai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnQuaylai.setForeground(new java.awt.Color(255, 255, 255));
        btnQuaylai.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\troLai.png")); // NOI18N
        btnQuaylai.setText("Quay lại");
        btnQuaylai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnQuaylaiMouseClicked(evt);
            }
        });
        btnQuaylai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuaylaiActionPerformed(evt);
            }
        });

        btnEyeMK.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\Eye.png")); // NOI18N
        btnEyeMK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEyeMKActionPerformed(evt);
            }
        });

        btnEyeMKMoi.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\Eye.png")); // NOI18N
        btnEyeMKMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEyeMKMoiActionPerformed(evt);
            }
        });

        btnEyeXacNhan.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\Eye.png")); // NOI18N
        btnEyeXacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEyeXacNhanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout formDoiMKLayout = new javax.swing.GroupLayout(formDoiMK);
        formDoiMK.setLayout(formDoiMKLayout);
        formDoiMKLayout.setHorizontalGroup(
            formDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formDoiMKLayout.createSequentialGroup()
                .addGroup(formDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formDoiMKLayout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(btnXacNhanDMK, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addComponent(btnQuaylai)
                        .addGap(0, 35, Short.MAX_VALUE))
                    .addGroup(formDoiMKLayout.createSequentialGroup()
                        .addGroup(formDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel59)
                            .addComponent(jLabel60)
                            .addComponent(jLabel58)
                            .addComponent(jLabel57))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(formDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtConfirmPassword)
                            .addComponent(txtNewPassword)
                            .addComponent(txtPassword)
                            .addComponent(txtUsername1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(formDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEyeMK, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEyeMKMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEyeXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(58, 58, 58))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formDoiMKLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel61)
                .addGap(121, 121, 121))
        );
        formDoiMKLayout.setVerticalGroup(
            formDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formDoiMKLayout.createSequentialGroup()
                .addGroup(formDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formDoiMKLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel61)
                        .addGap(62, 62, 62)
                        .addGroup(formDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(formDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtUsername1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(formDoiMKLayout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addGroup(formDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(30, 30, 30)
                        .addGroup(formDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formDoiMKLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEyeMK, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(btnEyeMKMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)))
                .addGroup(formDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(formDoiMKLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(btnEyeXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(59, 59, 59)
                .addGroup(formDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnQuaylai, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXacNhanDMK, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(74, 74, 74))
        );

        javax.swing.GroupLayout jpViewDoiMKLayout = new javax.swing.GroupLayout(jpViewDoiMK);
        jpViewDoiMK.setLayout(jpViewDoiMKLayout);
        jpViewDoiMKLayout.setHorizontalGroup(
            jpViewDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpViewDoiMKLayout.createSequentialGroup()
                .addGap(201, 201, 201)
                .addComponent(formDoiMK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(336, Short.MAX_VALUE))
        );
        jpViewDoiMKLayout.setVerticalGroup(
            jpViewDoiMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpViewDoiMKLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(formDoiMK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(214, Short.MAX_VALUE))
        );

        jpViewQLSPP.setBackground(new java.awt.Color(255, 255, 255));

        jLabel56.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(51, 102, 255));
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel56.setText("QUẢN LÝ SẢN PHẨM");

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel161.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel161.setForeground(new java.awt.Color(51, 102, 255));
        jLabel161.setText("Danh sách sản phẩm");

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 4, true));

        jLabel162.setText("Mã Sản phẩm:");

        jLabel94.setText("Tên sản phẩm:");

        jLabel108.setText("Danh mục:");

        jLabel163.setText("Nhà CC:");

        jLabel8.setText("Trạng thái");

        txtTenSP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        txtMaSanPham.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        rdoRun.setSelected(true);
        rdoRun.setText("Đang kinh doanh");

        rdoStop.setText("Ngừng kinh doanh");

        jLabel109.setText("Mô tả:");

        cbbDanhMuc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbbNhaCC.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtMotaSPP.setColumns(20);
        txtMotaSPP.setRows(5);
        jScrollPane15.setViewportView(txtMotaSPP);

        btnAddProductDetails.setBackground(new java.awt.Color(51, 102, 255));
        btnAddProductDetails.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAddProductDetails.setForeground(new java.awt.Color(255, 255, 255));
        btnAddProductDetails.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\xemCTSP.png")); // NOI18N
        btnAddProductDetails.setText("Chi tiết SP");
        btnAddProductDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductDetailsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel162, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel94))
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTenSP, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaSanPham, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addComponent(rdoRun)
                        .addGap(18, 18, 18)
                        .addComponent(rdoStop, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)))
                .addGap(60, 60, 60)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel108, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel163)
                    .addComponent(jLabel109, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbbDanhMuc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbbNhaCC, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
                .addGap(72, 72, 72)
                .addComponent(btnAddProductDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel108)
                            .addComponent(cbbDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel162))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel94)
                            .addComponent(jLabel163)
                            .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbNhaCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel8)
                                            .addComponent(rdoRun)
                                            .addComponent(rdoStop)))
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(17, 17, 17))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel109)
                                .addGap(35, 35, 35))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(btnAddProductDetails)
                        .addContainerGap(12, Short.MAX_VALUE))))
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 4, true));

        tblHienThiKetQua.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã SP", "Tên SP", "Danh mục", "Số lượng", "Mô tả", "Nhà cung cấp", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHienThiKetQua.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHienThiKetQuaMouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(tblHienThiKetQua);

        cbbLocTheoDM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel164.setText("Danh mục:");

        jLabel87.setText("Tìm kiếm:");

        txtTimKiemTheoMa.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        txtTimKiemTheoMa.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtTimKiemTheoMaCaretUpdate(evt);
            }
        });
        txtTimKiemTheoMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemTheoMaActionPerformed(evt);
            }
        });
        txtTimKiemTheoMa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimKiemTheoMaKeyPressed(evt);
            }
        });

        jLabel165.setText("Trạng thái:");

        cbbTrangThaiSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Đang kinh doanh", "Ngừng kinh doanh" }));

        jLabel110.setText("Nhà CC:");

        btnHoanTac1.setBackground(new java.awt.Color(51, 102, 255));
        btnHoanTac1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHoanTac1.setForeground(new java.awt.Color(255, 255, 255));
        btnHoanTac1.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\xemCTSP.png")); // NOI18N
        btnHoanTac1.setText("Xem tất cả");
        btnHoanTac1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoanTac1ActionPerformed(evt);
            }
        });

        cbbLocNhaCC.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnExcel.setBackground(new java.awt.Color(51, 102, 255));
        btnExcel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnExcel.setForeground(new java.awt.Color(255, 255, 255));
        btnExcel.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\xuatEX.png")); // NOI18N
        btnExcel.setText("Xuất Excel");
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });

        btnPreviousAll.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\veDau.png")); // NOI18N
        btnPreviousAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousAllActionPerformed(evt);
            }
        });

        btnPreviou.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\veTruoc.png")); // NOI18N
        btnPreviou.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviouActionPerformed(evt);
            }
        });

        btnNextt.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\veSau.png")); // NOI18N
        btnNextt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNexttActionPerformed(evt);
            }
        });

        btnNexttALL.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\veCuoi.png")); // NOI18N
        btnNexttALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNexttALLActionPerformed(evt);
            }
        });

        lblPages.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblPages.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPages.setText("1/2");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jScrollPane13)
                        .addContainerGap())
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel87)
                                .addGap(23, 23, 23)
                                .addComponent(txtTimKiemTheoMa))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel164)
                                .addGap(18, 18, 18)
                                .addComponent(cbbLocTheoDM, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel165)
                        .addGap(18, 18, 18)
                        .addComponent(cbbTrangThaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel110)
                        .addGap(18, 18, 18)
                        .addComponent(cbbLocNhaCC, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnHoanTac1, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                            .addComponent(btnExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(28, 28, 28))))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(326, 326, 326)
                .addComponent(btnPreviousAll, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnPreviou, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblPages, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnNextt, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnNexttALL, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel87)
                        .addComponent(txtTimKiemTheoMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnExcel))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbTrangThaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel110)
                    .addComponent(btnHoanTac1)
                    .addComponent(jLabel164)
                    .addComponent(jLabel165)
                    .addComponent(cbbLocTheoDM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbLocNhaCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNextt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblPages, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPreviou, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnNexttALL, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnPreviousAll, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel161)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel161)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpViewQLSPPLayout = new javax.swing.GroupLayout(jpViewQLSPP);
        jpViewQLSPP.setLayout(jpViewQLSPPLayout);
        jpViewQLSPPLayout.setHorizontalGroup(
            jpViewQLSPPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpViewQLSPPLayout.createSequentialGroup()
                .addGap(336, 336, 336)
                .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(323, 323, 323))
            .addGroup(jpViewQLSPPLayout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpViewQLSPPLayout.setVerticalGroup(
            jpViewQLSPPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpViewQLSPPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpViewQLSPP2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel111.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel111.setForeground(new java.awt.Color(51, 102, 255));
        jLabel111.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel111.setText("QUẢN LÝ CHI TIẾT SẢN PHẨM");

        btnBackProducts.setBackground(new java.awt.Color(51, 102, 255));
        btnBackProducts.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBackProducts.setForeground(new java.awt.Color(255, 255, 255));
        btnBackProducts.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\troLai.png")); // NOI18N
        btnBackProducts.setText("Quay lại");
        btnBackProducts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackProductsActionPerformed(evt);
            }
        });

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 4, true));

        jLabel93.setText("Mã SPCT:");

        txtMaSPCT1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        jLabel54.setText("Mã SP:");

        jLabel118.setText("Màu sắc:");

        cbbColor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel119.setText("Kích thước:");

        cbbSizes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel120.setText("Phong cách:");

        cbbStyle.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel121.setText("Số lượng:");

        txtQuantity.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        jLabel122.setText("Giá:");

        txtPrice.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        btnThemMoiThuocTinh.setBackground(new java.awt.Color(51, 102, 255));
        btnThemMoiThuocTinh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemMoiThuocTinh.setForeground(new java.awt.Color(255, 255, 255));
        btnThemMoiThuocTinh.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\add1.png")); // NOI18N
        btnThemMoiThuocTinh.setText("Thuộc tính");
        btnThemMoiThuocTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemMoiThuocTinhActionPerformed(evt);
            }
        });

        lblImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImage.setText("Ảnh");
        lblImage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 2));
        lblImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblImageMouseClicked(evt);
            }
        });

        pnWebCam.setBackground(new java.awt.Color(255, 255, 255));
        pnWebCam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnWebCam.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\QRcodenho.png")); // NOI18N
        pnWebCam.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 51, 51), 1, true));

        txtMaSP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel54)
                            .addComponent(jLabel118)
                            .addComponent(jLabel93))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbColor, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaSPCT1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(jLabel120)
                                .addGap(0, 12, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel121, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel122, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbStyle, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel119)
                        .addGap(18, 18, 18)
                        .addComponent(cbbSizes, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnWebCam, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnThemMoiThuocTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel93)
                            .addComponent(txtMaSPCT1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel120)
                            .addComponent(cbbStyle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel121)
                            .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel54))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel118)
                                .addComponent(cbbColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel122))
                                .addGap(3, 3, 3)))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel119)
                                    .addComponent(cbbSizes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(btnThemMoiThuocTinh, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(pnWebCam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                        .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)))
                .addContainerGap())
        );

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));
        jPanel31.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 4, true));

        jLabel112.setText("Tìm kiếm:");

        txtSearhProductDetail.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        txtSearhProductDetail.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtSearhProductDetailCaretUpdate(evt);
            }
        });

        cbbMauSacSPCT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel116.setText("Màu sắc:");

        jLabel114.setText("Loại SP:");

        jLabel115.setText("Kích thước:");

        cbbKichThuocSPCT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbbLoaiSPCT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel117.setText("Phong cách:");

        cbbStyles.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnXemDSSPCT.setBackground(new java.awt.Color(51, 102, 255));
        btnXemDSSPCT.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXemDSSPCT.setForeground(new java.awt.Color(255, 255, 255));
        btnXemDSSPCT.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\xemCTSP.png")); // NOI18N
        btnXemDSSPCT.setText("Xem tất cả ");
        btnXemDSSPCT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXemDSSPCTActionPerformed(evt);
            }
        });

        btnQRcode.setBackground(new java.awt.Color(51, 102, 255));
        btnQRcode.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnQRcode.setForeground(new java.awt.Color(255, 255, 255));
        btnQRcode.setText("Quét QR");
        btnQRcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQRcodeActionPerformed(evt);
            }
        });

        tblSPCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã SPCT", "Mã SP", "Tên SP", "Danh mục", "Màu sắc", "Kích thước", "Image_Url", "Phong cách", "Số lượng", "Giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSPCT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSPCTMouseClicked(evt);
            }
        });
        jScrollPane16.setViewportView(tblSPCT);

        jLabel168.setText("Số lượng:");

        jLabel169.setText("Giá bán:");

        cbbSoLuong.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbbSoLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbSoLuongActionPerformed(evt);
            }
        });

        cbbGiaBan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnExcelProductDetail.setBackground(new java.awt.Color(51, 102, 255));
        btnExcelProductDetail.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnExcelProductDetail.setForeground(new java.awt.Color(255, 255, 255));
        btnExcelProductDetail.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\xuatEX.png")); // NOI18N
        btnExcelProductDetail.setText("Xuất Excel");
        btnExcelProductDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelProductDetailActionPerformed(evt);
            }
        });

        btnDowloadQr.setBackground(new java.awt.Color(51, 102, 255));
        btnDowloadQr.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDowloadQr.setForeground(new java.awt.Color(255, 255, 255));
        btnDowloadQr.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\luuTru.png")); // NOI18N
        btnDowloadQr.setText("QR Code");
        btnDowloadQr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDowloadQrActionPerformed(evt);
            }
        });

        btnPreviouAll.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\veDau.png")); // NOI18N
        btnPreviouAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviouAllActionPerformed(evt);
            }
        });

        btnPrevious.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\veTruoc.png")); // NOI18N
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\veSau.png")); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnNextAll.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\veCuoi.png")); // NOI18N
        btnNextAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextAllActionPerformed(evt);
            }
        });

        lblPage.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblPage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPage.setText("0/7");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(346, 346, 346)
                .addComponent(btnPreviouAll, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblPage, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNextAll, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 980, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 15, Short.MAX_VALUE))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel117)
                            .addComponent(jLabel114, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel112, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cbbLoaiSPCT, javax.swing.GroupLayout.Alignment.LEADING, 0, 152, Short.MAX_VALUE)
                            .addComponent(txtSearhProductDetail, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbbStyles, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel31Layout.createSequentialGroup()
                                        .addGap(43, 43, 43)
                                        .addComponent(jLabel115))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel116)))
                                .addGap(25, 25, 25)
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cbbKichThuocSPCT, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbbMauSacSPCT, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(47, 47, 47)
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel168, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel169, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cbbSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbbGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(btnQRcode, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnExcelProductDetail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDowloadQr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnXemDSSPCT, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
                        .addGap(14, 14, 14))))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel112)
                        .addComponent(txtSearhProductDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnQRcode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDowloadQr)))
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel114)
                            .addComponent(cbbLoaiSPCT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel115)
                            .addComponent(cbbKichThuocSPCT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel168)
                            .addComponent(btnExcelProductDetail))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbbMauSacSPCT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel116)
                            .addComponent(cbbStyles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel117)
                            .addComponent(jLabel169)
                            .addComponent(cbbGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(btnXemDSSPCT)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPreviouAll)
                    .addComponent(btnPrevious)
                    .addComponent(lblPage, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext)
                    .addComponent(btnNextAll))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jLabel52.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(51, 102, 255));
        jLabel52.setText("Danh sách sản phẩm chi tiết:");

        javax.swing.GroupLayout jpViewQLSPP2Layout = new javax.swing.GroupLayout(jpViewQLSPP2);
        jpViewQLSPP2.setLayout(jpViewQLSPP2Layout);
        jpViewQLSPP2Layout.setHorizontalGroup(
            jpViewQLSPP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpViewQLSPP2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpViewQLSPP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpViewQLSPP2Layout.createSequentialGroup()
                        .addComponent(btnBackProducts)
                        .addGap(287, 287, 287)
                        .addComponent(jLabel111, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jpViewQLSPP2Layout.setVerticalGroup(
            jpViewQLSPP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpViewQLSPP2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpViewQLSPP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBackProducts)
                    .addComponent(jLabel111, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel52)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jpViewQLSPP3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel124.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel124.setForeground(new java.awt.Color(0, 102, 255));
        jLabel124.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel124.setText("QUẢN LÝ THUỘC TÍNH");

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));

        tblMauSac.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã màu", "Tên màu", "Mô tả"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMauSac.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMauSacMouseClicked(evt);
            }
        });
        jScrollPane17.setViewportView(tblMauSac);

        jLabel125.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel125.setForeground(new java.awt.Color(255, 102, 102));
        jLabel125.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel125.setText("Quản lý màu sắc");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel125, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel125, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        jLabel126.setText("Mã màu:");

        jLabel127.setText("Tên màu sắc:");

        jLabel128.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel128.setForeground(new java.awt.Color(0, 51, 255));
        jLabel128.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel128.setText("Thêm mới màu sắc");

        txtMaMau.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        txtTenMau.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel126)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtMaMau, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(jLabel127)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTenMau))))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jLabel128, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel128)
                .addGap(49, 49, 49)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaMau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel126))
                .addGap(46, 46, 46)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel127)
                    .addComponent(txtTenMau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(259, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(34, 34, 34))
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tbpThuocTinh.addTab("Màu sắc", jPanel13);

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));

        tblKichThuoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã kích thước", "Tên kích thước", "Mô tả"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKichThuoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKichThuocMouseClicked(evt);
            }
        });
        jScrollPane18.setViewportView(tblKichThuoc);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        jLabel129.setText("Mã KT:");

        jLabel130.setText("Tên KT");

        jLabel131.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel131.setForeground(new java.awt.Color(0, 51, 255));
        jLabel131.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel131.setText("Thêm mới kích thước");

        txtMaKT.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        txtTenKichThuoc.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        jLabel166.setText("Mô tả:");

        txtMoTaSize.setColumns(20);
        txtMoTaSize.setRows(5);
        jScrollPane21.setViewportView(txtMoTaSize);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel129, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel166, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel131, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(76, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtTenKichThuoc, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                                    .addComponent(txtMaKT)
                                    .addComponent(jScrollPane21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGap(38, 38, 38))))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel130)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel131)
                .addGap(18, 18, 18)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel129)
                    .addComponent(txtMaKT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel130)
                    .addComponent(txtTenKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel166)
                    .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(211, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addGap(0, 23, Short.MAX_VALUE)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel132.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel132.setForeground(new java.awt.Color(255, 102, 102));
        jLabel132.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel132.setText("Quản lý kích thước");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(417, 417, 417)
                .addComponent(jLabel132)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel132)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(91, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tbpThuocTinh.addTab("Kích thước", jPanel14);

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));

        jLabel133.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel133.setForeground(new java.awt.Color(255, 102, 102));
        jLabel133.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel133.setText("Quản lý nhà cung cấp");

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        jLabel134.setText("Mã NCC:");

        jLabel135.setText("Tên NCC:");

        jLabel136.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel136.setForeground(new java.awt.Color(0, 51, 255));
        jLabel136.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel136.setText("Thêm mới nhà cung cấp");

        txtNCC.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        txtTenNCC.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        jLabel137.setText("SĐT:");

        txtSDTNCC.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        jLabel138.setText("Email:");

        txtEmailNCC.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        jLabel139.setText("Địa chỉ:");

        txtDiaChiNCC.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel135)
                                    .addComponent(jLabel137)
                                    .addComponent(jLabel138)
                                    .addComponent(jLabel139))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtDiaChiNCC, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                    .addComponent(txtEmailNCC)
                                    .addComponent(txtTenNCC)
                                    .addComponent(txtSDTNCC)))
                            .addComponent(jLabel134, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel28Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(txtNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel28Layout.createSequentialGroup()
                            .addGap(63, 63, 63)
                            .addComponent(jLabel136))))
                .addGap(0, 64, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel136)
                .addGap(34, 34, 34)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel134)
                    .addComponent(txtNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel135)
                    .addComponent(txtTenNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSDTNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel137))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel138)
                    .addComponent(txtEmailNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel139)
                    .addComponent(txtDiaChiNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(203, Short.MAX_VALUE))
        );

        tblNhaCungCap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã NCC", "Tên NCC", "SĐT", "Email", "ĐỊa chỉ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNhaCungCap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhaCungCapMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tblNhaCungCap);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(379, 379, 379)
                .addComponent(jLabel133)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 628, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel133)
                .addGap(87, 87, 87)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE))
                .addGap(0, 94, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tbpThuocTinh.addTab("Nhà cung cấp", jPanel15);

        jPanel20.setPreferredSize(new java.awt.Dimension(949, 600));

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));

        tblPhongCach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã PC", "Tên PC", "Mô tả"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPhongCach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPhongCachMouseClicked(evt);
            }
        });
        jScrollPane19.setViewportView(tblPhongCach);

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));
        jPanel32.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        jLabel141.setText("Mã PC:");

        jLabel142.setText("Tên PC:");

        jLabel143.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel143.setForeground(new java.awt.Color(0, 51, 255));
        jLabel143.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel143.setText("Thêm mới phong cách");

        txtMaPC.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        txtTenPhongCach.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        jLabel123.setText("Mô tả;");

        txtMoTaPC.setColumns(20);
        txtMoTaPC.setRows(5);
        jScrollPane22.setViewportView(txtMoTaPC);

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel32Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel32Layout.createSequentialGroup()
                        .addComponent(jLabel123, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel32Layout.createSequentialGroup()
                            .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel142, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel141, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(42, 42, 42)
                            .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtMaPC, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                                .addComponent(txtTenPhongCach)))
                        .addGroup(jPanel32Layout.createSequentialGroup()
                            .addGap(63, 63, 63)
                            .addComponent(jLabel143, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(55, 55, 55))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel143)
                .addGap(36, 36, 36)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel141)
                    .addComponent(txtMaPC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel142)
                    .addComponent(txtTenPhongCach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel123)
                    .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(174, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE))
                .addContainerGap(118, Short.MAX_VALUE))
        );

        jLabel140.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel140.setForeground(new java.awt.Color(255, 102, 102));
        jLabel140.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel140.setText("Quản lý phong cách");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(390, 390, 390)
                .addComponent(jLabel140)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel140)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tbpThuocTinh.addTab("Phong cách", jPanel20);

        btnBackkkkkk.setBackground(new java.awt.Color(51, 102, 255));
        btnBackkkkkk.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBackkkkkk.setForeground(new java.awt.Color(255, 255, 255));
        btnBackkkkkk.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\troLai.png")); // NOI18N
        btnBackkkkkk.setText("Quay lại");
        btnBackkkkkk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackkkkkkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpViewQLSPP3Layout = new javax.swing.GroupLayout(jpViewQLSPP3);
        jpViewQLSPP3.setLayout(jpViewQLSPP3Layout);
        jpViewQLSPP3Layout.setHorizontalGroup(
            jpViewQLSPP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpViewQLSPP3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpViewQLSPP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpViewQLSPP3Layout.createSequentialGroup()
                        .addComponent(btnBackkkkkk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel124, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(316, 316, 316))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpViewQLSPP3Layout.createSequentialGroup()
                        .addComponent(tbpThuocTinh)
                        .addContainerGap())))
        );
        jpViewQLSPP3Layout.setVerticalGroup(
            jpViewQLSPP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpViewQLSPP3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpViewQLSPP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel124, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBackkkkkk))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tbpThuocTinh))
        );

        jpViewQLKH.setBackground(new java.awt.Color(255, 255, 255));
        jpViewQLKH.setPreferredSize(new java.awt.Dimension(948, 600));

        jLabel170.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel170.setText("Quản Lý Khách Hàng");

        DanhSachKhachHang1.setBackground(new java.awt.Color(255, 255, 255));
        DanhSachKhachHang1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        tblKhachH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã khách hàng", "Tên khách hàng", "SĐT", "Điểm tích lũy", "Điểm đã dùng", "Điểm còn lại"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKhachH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhachHMouseClicked(evt);
            }
        });
        jScrollPane23.setViewportView(tblKhachH);

        jLabel171.setText("Tên khách hàng");

        txtITenKhachHang1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        txtSoDiienThoai.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        jLabel172.setText("SĐT");

        jLabel173.setText("Điểm tích lũy");

        txtDiemTichLuy.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        jLabel174.setText("Điểm đã dùng");

        txtTinhDiem.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        jLabel175.setText("Điểm còn lại");

        txtDiemDaDung.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        btnThemMoiKH1.setBackground(new java.awt.Color(51, 102, 255));
        btnThemMoiKH1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemMoiKH1.setForeground(new java.awt.Color(255, 255, 255));
        btnThemMoiKH1.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\add.png")); // NOI18N
        btnThemMoiKH1.setText("Thêm");
        btnThemMoiKH1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemMoiKH1ActionPerformed(evt);
            }
        });

        btnLamMoiKH1.setBackground(new java.awt.Color(51, 102, 255));
        btnLamMoiKH1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLamMoiKH1.setForeground(new java.awt.Color(255, 255, 255));
        btnLamMoiKH1.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\luuTru1.png")); // NOI18N
        btnLamMoiKH1.setText("Lưu");
        btnLamMoiKH1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiKH1ActionPerformed(evt);
            }
        });

        btnSuaKhachHang1.setBackground(new java.awt.Color(51, 102, 255));
        btnSuaKhachHang1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSuaKhachHang1.setForeground(new java.awt.Color(255, 255, 255));
        btnSuaKhachHang1.setIcon(new javax.swing.ImageIcon("C:\\Users\\DƯƠNG MINH HÀ\\Desktop\\anhDuAn1\\penSua.png")); // NOI18N
        btnSuaKhachHang1.setText("Cập nhật");
        btnSuaKhachHang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaKhachHang1ActionPerformed(evt);
            }
        });

        jLabel176.setText("Mã khách hàng");

        txtMaKH.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout DanhSachKhachHang1Layout = new javax.swing.GroupLayout(DanhSachKhachHang1);
        DanhSachKhachHang1.setLayout(DanhSachKhachHang1Layout);
        DanhSachKhachHang1Layout.setHorizontalGroup(
            DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DanhSachKhachHang1Layout.createSequentialGroup()
                .addGroup(DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DanhSachKhachHang1Layout.createSequentialGroup()
                        .addGap(131, 131, 131)
                        .addGroup(DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel172)
                            .addComponent(jLabel171)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DanhSachKhachHang1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel174, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel175, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel176, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel173, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(36, 36, 36)
                .addGroup(DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DanhSachKhachHang1Layout.createSequentialGroup()
                        .addGroup(DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtITenKhachHang1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaKH, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnThemMoiKH1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(DanhSachKhachHang1Layout.createSequentialGroup()
                        .addGroup(DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtSoDiienThoai)
                            .addComponent(txtDiemTichLuy)
                            .addComponent(txtTinhDiem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(txtDiemDaDung, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnLamMoiKH1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSuaKhachHang1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(139, 139, 139))
            .addGroup(DanhSachKhachHang1Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );
        DanhSachKhachHang1Layout.setVerticalGroup(
            DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DanhSachKhachHang1Layout.createSequentialGroup()
                .addGroup(DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DanhSachKhachHang1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel176)
                            .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(DanhSachKhachHang1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(btnThemMoiKH1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtITenKhachHang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel171))
                .addGap(15, 15, 15)
                .addGroup(DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSoDiienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel172)
                    .addComponent(btnSuaKhachHang1))
                .addGap(14, 14, 14)
                .addGroup(DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDiemTichLuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel173))
                .addGap(10, 10, 10)
                .addGroup(DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTinhDiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel174)
                    .addComponent(btnLamMoiKH1))
                .addGap(18, 18, 18)
                .addGroup(DanhSachKhachHang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel175)
                    .addComponent(txtDiemDaDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout jpViewQLKHLayout = new javax.swing.GroupLayout(jpViewQLKH);
        jpViewQLKH.setLayout(jpViewQLKHLayout);
        jpViewQLKHLayout.setHorizontalGroup(
            jpViewQLKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpViewQLKHLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpViewQLKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpViewQLKHLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel170)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(DanhSachKhachHang1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpViewQLKHLayout.setVerticalGroup(
            jpViewQLKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpViewQLKHLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel170)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DanhSachKhachHang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpViewTK.setBackground(new java.awt.Color(255, 255, 255));
        jpViewTK.setPreferredSize(new java.awt.Dimension(948, 600));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 51, 51));
        jLabel14.setText("Thống kê sản phẩm");

        ThongKeSP.setBackground(new java.awt.Color(255, 255, 255));
        ThongKeSP.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel40.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel40.setText("Tìm kiếm sản phẩm");

        txtTimKiemSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemSPActionPerformed(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel41.setText("Lọc theo khoảng Date");

        jLabel42.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel42.setText("Từ");

        jLabel43.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel43.setText("Đến");

        tongSP.setBackground(new java.awt.Color(102, 204, 255));

        jLabel44.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel44.setText("Tổng số sản phẩm:");

        lblTongSP.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblTongSP.setText("0 SP");

        javax.swing.GroupLayout tongSPLayout = new javax.swing.GroupLayout(tongSP);
        tongSP.setLayout(tongSPLayout);
        tongSPLayout.setHorizontalGroup(
            tongSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tongSPLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jLabel44)
                .addGap(14, 14, 14))
            .addGroup(tongSPLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(lblTongSP, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tongSPLayout.setVerticalGroup(
            tongSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tongSPLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel44)
                .addGap(18, 18, 18)
                .addComponent(lblTongSP, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        spDaBan.setBackground(new java.awt.Color(255, 204, 204));

        jLabel45.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel45.setText("Tổng sản phẩm bán:");

        lblSPDaBan.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblSPDaBan.setText("0 SP");

        javax.swing.GroupLayout spDaBanLayout = new javax.swing.GroupLayout(spDaBan);
        spDaBan.setLayout(spDaBanLayout);
        spDaBanLayout.setHorizontalGroup(
            spDaBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(spDaBanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(spDaBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblSPDaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        spDaBanLayout.setVerticalGroup(
            spDaBanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(spDaBanLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel45)
                .addGap(18, 18, 18)
                .addComponent(lblSPDaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        btnTimKiemSP.setText("Tìm");
        btnTimKiemSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemSPActionPerformed(evt);
            }
        });

        jTuNgay.setDateFormatString("yyyy-MM-dd");
        jTuNgay.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTuNgayPropertyChange(evt);
            }
        });

        jDenNgay.setDateFormatString("yyyy-MM-dd");
        jDenNgay.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDenNgayPropertyChange(evt);
            }
        });

        btnLocKhoangSanPham.setText("Lọc");
        btnLocKhoangSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocKhoangSanPhamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ThongKeSPLayout = new javax.swing.GroupLayout(ThongKeSP);
        ThongKeSP.setLayout(ThongKeSPLayout);
        ThongKeSPLayout.setHorizontalGroup(
            ThongKeSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThongKeSPLayout.createSequentialGroup()
                .addGroup(ThongKeSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ThongKeSPLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(ThongKeSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                            .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ThongKeSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTuNgay, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                            .addComponent(jDenNgay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(btnLocKhoangSanPham))
                    .addGroup(ThongKeSPLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(ThongKeSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel41)
                            .addComponent(jLabel40))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimKiemSP, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTimKiemSP)))
                .addGap(51, 51, 51)
                .addComponent(tongSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(spDaBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ThongKeSPLayout.setVerticalGroup(
            ThongKeSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThongKeSPLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(ThongKeSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(txtTimKiemSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiemSP))
                .addGap(18, 18, 18)
                .addComponent(jLabel41)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ThongKeSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42)
                    .addComponent(jTuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLocKhoangSanPham))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(ThongKeSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDenNgay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ThongKeSPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ThongKeSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spDaBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tongSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        jLabel46.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 51, 51));
        jLabel46.setText("Danh sách sản phẩm đã bán");

        tblDanhSachSPdaBan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã Sản Phẩm", "Tên Sản Phẩm", "Mô tả", "Danh Mục", "Trạng Thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDanhSachSPdaBan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDanhSachSPdaBanMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblDanhSachSPdaBan);

        jLabel47.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 51, 51));
        jLabel47.setText("Thống kê doanh thu");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel48.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel48.setText("Lọc theo khoảng:");

        jLabel49.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel49.setText("Từ:");

        jLabel50.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel50.setText("Đến:");

        jPanel6.setBackground(new java.awt.Color(0, 0, 255));

        jLabel53.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("Doanh Thu Tháng");

        lblDoanhThuThang.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblDoanhThuThang.setForeground(new java.awt.Color(255, 255, 255));
        lblDoanhThuThang.setText("0 VNĐ");

        jLabel113.setText("Tháng");

        jLabel145.setText("Năm");

        jDTTThang.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDTTThangPropertyChange(evt);
            }
        });

        jDTTNam.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDTTNamPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel53)
                    .addComponent(jLabel113, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jDTTThang, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel145, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41))
                            .addComponent(jDTTNam, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(lblDoanhThuThang, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel53)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel113, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel145, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDTTThang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDTTNam, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(lblDoanhThuThang)
                .addGap(24, 24, 24))
        );

        jPanel7.setBackground(new java.awt.Color(204, 0, 204));

        jLabel55.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("Doanh thu ngày");

        lblDoanhThuNgay.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblDoanhThuNgay.setForeground(new java.awt.Color(255, 255, 255));
        lblDoanhThuNgay.setText("0 VNĐ");

        jDateDoanhThuNgay.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateDoanhThuNgayPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateDoanhThuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(lblDoanhThuNgay)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel55)
                .addGap(44, 44, 44)
                .addComponent(jDateDoanhThuNgay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(26, 26, 26)
                .addComponent(lblDoanhThuNgay)
                .addGap(20, 20, 20))
        );

        jPanel5.setBackground(new java.awt.Color(102, 102, 102));

        jLabel51.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Doanh Thu tổng");

        lblDoanhThuTong.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblDoanhThuTong.setForeground(new java.awt.Color(255, 255, 255));
        lblDoanhThuTong.setText("0 VNĐ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDoanhThuTong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDoanhThuTong)
                .addGap(44, 44, 44))
        );

        TuLocTheoKhoangUpdate.setDateFormatString("yyyy-MM-dd");
        TuLocTheoKhoangUpdate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                TuLocTheoKhoangUpdatePropertyChange(evt);
            }
        });

        DenLocTheoKhoangUpdate.setDateFormatString("yyyy-MM-dd");
        DenLocTheoKhoangUpdate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                DenLocTheoKhoangUpdatePropertyChange(evt);
            }
        });

        btnLocDoanhSoUpdate.setText("Lọc");
        btnLocDoanhSoUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocDoanhSoUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(DenLocTheoKhoangUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel48)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TuLocTheoKhoangUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(btnLocDoanhSoUpdate)
                                        .addGap(45, 45, 45)))))))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(135, 135, 135))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel48)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel49)
                            .addComponent(TuLocTheoKhoangUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(DenLocTheoKhoangUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel50))
                        .addGap(18, 18, 18)
                        .addComponent(btnLocDoanhSoUpdate)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(44, 44, 44))
        );

        cbbTOP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Top 10 sản phẩm bán nhiều nhất", "Top 10 sản phẩm bán ít nhất" }));
        cbbTOP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbTOPActionPerformed(evt);
            }
        });

        btnLamMoiThongKe.setText("F5");
        btnLamMoiThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiThongKeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpViewTKLayout = new javax.swing.GroupLayout(jpViewTK);
        jpViewTK.setLayout(jpViewTKLayout);
        jpViewTKLayout.setHorizontalGroup(
            jpViewTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpViewTKLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpViewTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addGroup(jpViewTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(ThongKeSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 937, Short.MAX_VALUE)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpViewTKLayout.createSequentialGroup()
                            .addGap(9, 9, 9)
                            .addGroup(jpViewTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jpViewTKLayout.createSequentialGroup()
                                    .addComponent(jLabel46)
                                    .addGap(350, 350, 350)
                                    .addComponent(btnLamMoiThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(cbbTOP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpViewTKLayout.setVerticalGroup(
            jpViewTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpViewTKLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ThongKeSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(jpViewTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbTOP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoiThongKe)
                    .addComponent(jLabel46))
                .addGap(7, 7, 7)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpViewQLKM.setBackground(new java.awt.Color(255, 255, 255));
        jpViewQLKM.setPreferredSize(new java.awt.Dimension(948, 600));

        jLabel76.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel76.setText("Chương trình khuyến mãi");

        TabGiamGia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabGiamGiaMouseClicked(evt);
            }
        });

        Coupon.setBackground(new java.awt.Color(255, 255, 255));
        Coupon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblCoupon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã giảm giá", "Code", "Mô tả", "Giá trị chiết khấu", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái", "Tiền áp dụng", "Tiền giảm tối đa", "Số lượng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCoupon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCouponMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tblCoupon);

        btnAddCou.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddCou.setText("Thêm mới");
        btnAddCou.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddCouMouseClicked(evt);
            }
        });
        btnAddCou.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCouActionPerformed(evt);
            }
        });

        btnEditCou.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEditCou.setText("Cập nhật");
        btnEditCou.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditCouMouseClicked(evt);
            }
        });
        btnEditCou.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditCouActionPerformed(evt);
            }
        });

        jLabel98.setText("Mã giảm giá");

        txtMaGiamGia.setForeground(new java.awt.Color(51, 255, 204));
        txtMaGiamGia.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 255)));

        jLabel99.setText("code");

        txtCode.setForeground(new java.awt.Color(51, 255, 204));
        txtCode.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 255)));

        jLabel100.setText("Phần trăm giảm giá");

        txtPhanTramChietKhau.setForeground(new java.awt.Color(255, 102, 102));
        txtPhanTramChietKhau.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 255)));
        txtPhanTramChietKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhanTramChietKhauActionPerformed(evt);
            }
        });

        jLabel101.setText("Mô tả");

        txtMoTa.setColumns(20);
        txtMoTa.setForeground(new java.awt.Color(255, 0, 255));
        txtMoTa.setRows(5);
        jScrollPane11.setViewportView(txtMoTa);

        jLabel102.setText("Ngày bắt đầu");

        jLabel103.setText("Ngày kết thúc");

        jLabel104.setText("Trạng thái");

        rdoDangKM.setSelected(true);
        rdoDangKM.setText("Đang khuyến mãi");

        rdoDungKM.setText("Dừng khuyến mãi");

        btnVoHH.setBackground(new java.awt.Color(255, 102, 102));
        btnVoHH.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnVoHH.setText("Vô hiệu hóa");
        btnVoHH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVoHHMouseClicked(evt);
            }
        });

        btnLuu.setBackground(new java.awt.Color(255, 255, 153));
        btnLuu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLuu.setText("Lưu");
        btnLuu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLuuMouseClicked(evt);
            }
        });
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        txt_tien_toi_da.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 102, 255)));
        txt_tien_toi_da.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_tien_toi_daActionPerformed(evt);
            }
        });

        txt_tien_ap_dung.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 255)));

        jLabel107.setText("Tiền giảm tối đa");

        jLabel167.setText("Tiền áp dụng:");

        jLabel177.setText("Số lượng:");

        txt_so_luong_vou.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 102, 255)));
        txt_so_luong_vou.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_so_luong_vouActionPerformed(evt);
            }
        });

        jdc_ngay_bat_dau_voucher.setDateFormatString("yyyy-MM-dd");

        jdc_ngay_ket_thuc_voucher.setDateFormatString("yyyy-MM-dd");

        jLabel178.setForeground(new java.awt.Color(255, 0, 0));
        jLabel178.setText("%");

        capNhatTichDiem.setBackground(new java.awt.Color(255, 255, 255));
        capNhatTichDiem.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cập nhật tích điểm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jLabel77.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel77.setText("Tỉ lệ quy đổi điểm thưởng");

        jLabel78.setText("VND");

        jLabel79.setText("=");

        jLabel80.setText("Thanh toán bằng điểm");

        jLabel81.setText("Điểm");

        jLabel82.setText("=");

        jLabel83.setText("VND");

        btnLuuThayDoi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLuuThayDoi.setText("Lưu thay đổi");
        btnLuuThayDoi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLuuThayDoiMouseClicked(evt);
            }
        });

        btnResetPoint.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnResetPoint.setText("Làm mới");
        btnResetPoint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetPointMouseClicked(evt);
            }
        });

        jLabel84.setText("Điểm");

        javax.swing.GroupLayout capNhatTichDiemLayout = new javax.swing.GroupLayout(capNhatTichDiem);
        capNhatTichDiem.setLayout(capNhatTichDiemLayout);
        capNhatTichDiemLayout.setHorizontalGroup(
            capNhatTichDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(capNhatTichDiemLayout.createSequentialGroup()
                .addGroup(capNhatTichDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(capNhatTichDiemLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel80))
                    .addGroup(capNhatTichDiemLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(capNhatTichDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(capNhatTichDiemLayout.createSequentialGroup()
                                .addComponent(txtMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel78)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel79)
                                .addGap(88, 88, 88)
                                .addComponent(jLabel84))))
                    .addGroup(capNhatTichDiemLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(capNhatTichDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnLuuThayDoi, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(capNhatTichDiemLayout.createSequentialGroup()
                                .addComponent(txtMoney1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel81)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel82, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6)
                        .addGroup(capNhatTichDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnResetPoint)
                            .addGroup(capNhatTichDiemLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(capNhatTichDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtPoint1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        capNhatTichDiemLayout.setVerticalGroup(
            capNhatTichDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(capNhatTichDiemLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel77)
                .addGap(30, 30, 30)
                .addGroup(capNhatTichDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel78)
                    .addComponent(jLabel79)
                    .addComponent(txtPoint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel84))
                .addGap(36, 36, 36)
                .addComponent(jLabel80)
                .addGap(29, 29, 29)
                .addGroup(capNhatTichDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPoint1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel81)
                    .addComponent(jLabel82)
                    .addComponent(jLabel83)
                    .addComponent(txtMoney1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62)
                .addGroup(capNhatTichDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnResetPoint)
                    .addComponent(btnLuuThayDoi))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel179.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel179.setForeground(new java.awt.Color(255, 0, 255));
        jLabel179.setText("Bảng thông tin Voucher");

        javax.swing.GroupLayout CouponLayout = new javax.swing.GroupLayout(Coupon);
        Coupon.setLayout(CouponLayout);
        CouponLayout.setHorizontalGroup(
            CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CouponLayout.createSequentialGroup()
                .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(CouponLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CouponLayout.createSequentialGroup()
                                .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CouponLayout.createSequentialGroup()
                                        .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel98)
                                            .addComponent(jLabel99)
                                            .addComponent(jLabel100, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                                        .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(CouponLayout.createSequentialGroup()
                                                .addComponent(txtPhanTramChietKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel178))
                                            .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txtCode, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                                                .addComponent(txtMaGiamGia)))
                                        .addGap(79, 79, 79))
                                    .addGroup(CouponLayout.createSequentialGroup()
                                        .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel101)
                                            .addComponent(jLabel104))
                                        .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(CouponLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(rdoDangKM)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(rdoDungKM))
                                            .addGroup(CouponLayout.createSequentialGroup()
                                                .addGap(79, 79, 79)
                                                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                                        .addGap(40, 40, 40)))
                                .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel177)
                                        .addComponent(jLabel107)
                                        .addComponent(jLabel102)
                                        .addComponent(jLabel167))
                                    .addGroup(CouponLayout.createSequentialGroup()
                                        .addComponent(jLabel103)
                                        .addGap(15, 15, 15)))
                                .addGap(36, 36, 36)
                                .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jdc_ngay_bat_dau_voucher, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txt_so_luong_vou, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                                            .addComponent(txt_tien_ap_dung, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_tien_toi_da, javax.swing.GroupLayout.Alignment.LEADING)))
                                    .addComponent(jdc_ngay_ket_thuc_voucher, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(35, 35, 35)
                                .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnVoHH)
                                    .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btnAddCou, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnEditCou, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnLuu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))))
                            .addGroup(CouponLayout.createSequentialGroup()
                                .addComponent(jLabel179, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addComponent(capNhatTichDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)))
                .addContainerGap())
        );
        CouponLayout.setVerticalGroup(
            CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CouponLayout.createSequentialGroup()
                .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CouponLayout.createSequentialGroup()
                        .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CouponLayout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtMaGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel98)
                                    .addComponent(jLabel167)
                                    .addComponent(txt_tien_ap_dung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(CouponLayout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(btnAddCou)))
                        .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(CouponLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel99))
                                    .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txt_tien_toi_da, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel107)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel178)
                                        .addComponent(txt_so_luong_vou, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel177))
                                    .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtPhanTramChietKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel100)))
                                .addGap(23, 23, 23)
                                .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel101)
                                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(CouponLayout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jdc_ngay_bat_dau_voucher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel102))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel103, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jdc_ngay_ket_thuc_voucher, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(CouponLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(btnEditCou)
                                .addGap(26, 26, 26)
                                .addComponent(btnLuu)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnVoHH, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(CouponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoDangKM)
                            .addComponent(rdoDungKM)
                            .addComponent(jLabel104))
                        .addGap(54, 54, 54)
                        .addComponent(jLabel179, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CouponLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(capNhatTichDiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        TabGiamGia.addTab("Voucher giảm giá", Coupon);

        Discount.setBackground(new java.awt.Color(255, 255, 255));
        Discount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Discount.setPreferredSize(new java.awt.Dimension(600, 505));

        tblDiscount.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã chiết khấu", "Code chi tiết", "Ngày bắt đầu", "Ngày kết thúc", "Số tiền chiết khấu", "Màu", "Kiểu dáng", "Kích thước"
            }
        ));
        tblDiscount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDiscountMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tblDiscount);

        btnAddDis.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddDis.setText("Thêm mới");
        btnAddDis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddDisMouseClicked(evt);
            }
        });

        btnEditDis.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEditDis.setText("Cập nhật");
        btnEditDis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditDisMouseClicked(evt);
            }
        });
        btnEditDis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditDisActionPerformed(evt);
            }
        });

        jLabel105.setText("Mã chiết khấu");

        jLabel106.setText("Code");

        txtMa_chiet_khau.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(51, 204, 0)));
        txtMa_chiet_khau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMa_chiet_khauActionPerformed(evt);
            }
        });

        jLabel10.setText("Ngày bắt đầu");

        jLabel85.setText("Ngày kết thúc");

        btnLamMoi2.setBackground(new java.awt.Color(255, 255, 153));
        btnLamMoi2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLamMoi2.setText("Lưu");
        btnLamMoi2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLamMoi2MouseClicked(evt);
            }
        });

        jLabel180.setText("Số tiền giảm");

        txt_so_tien_giam.setForeground(new java.awt.Color(255, 102, 255));
        txt_so_tien_giam.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(153, 255, 0)));

        jdc_ngay_bat_dau.setDateFormatString("yyyy-MM-dd");

        jdc_ngay_ket_thuc.setDateFormatString("yyyy-MM-dd");

        tbl_san_pham_khuyen_mai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Code giảm giá chi tiết", "Giá", "Màu", "Kiểu dáng", "Kích thước"
            }
        ));
        tbl_san_pham_khuyen_mai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_san_pham_khuyen_maiMouseClicked(evt);
            }
        });
        jScrollPane24.setViewportView(tbl_san_pham_khuyen_mai);

        txt_tim_sp.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt_tim_spCaretUpdate(evt);
            }
        });
        txt_tim_sp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_tim_spKeyTyped(evt);
            }
        });

        jLabel181.setText("Tìm SP");

        txt_tim_chuong_trình.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt_tim_chuong_trìnhCaretUpdate(evt);
            }
        });

        jLabel183.setBackground(new java.awt.Color(153, 51, 255));
        jLabel183.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel183.setForeground(new java.awt.Color(255, 0, 255));
        jLabel183.setText("Bảng chương trình khuyến mãi ");

        txt_code_chi_tiet.setForeground(new java.awt.Color(255, 102, 255));
        txt_code_chi_tiet.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(153, 255, 0)));

        jLabel184.setText("Màu");

        jLabel151.setText("Kiểu dáng");

        jLabel152.setText("Kích thước");

        javax.swing.GroupLayout DiscountLayout = new javax.swing.GroupLayout(Discount);
        Discount.setLayout(DiscountLayout);
        DiscountLayout.setHorizontalGroup(
            DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DiscountLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DiscountLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel183, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(txt_tim_chuong_trình, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(jLabel182, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(DiscountLayout.createSequentialGroup()
                        .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane10)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DiscountLayout.createSequentialGroup()
                                .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(DiscountLayout.createSequentialGroup()
                                        .addComponent(jLabel105)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtMa_chiet_khau, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(DiscountLayout.createSequentialGroup()
                                        .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel181)
                                            .addComponent(jLabel184, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txt_tim_sp, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                                            .addComponent(txtMau))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel180)
                                    .addComponent(jLabel151, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(35, 35, 35)
                                .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_so_tien_giam, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                                    .addComponent(txtKieuDang))
                                .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(DiscountLayout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jLabel152, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(DiscountLayout.createSequentialGroup()
                                        .addGap(45, 45, 45)
                                        .addComponent(jLabel106, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_code_chi_tiet, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 18, Short.MAX_VALUE)
                                .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel85)
                                    .addComponent(jLabel10))
                                .addGap(34, 34, 34)
                                .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jdc_ngay_bat_dau, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                                    .addComponent(jdc_ngay_ket_thuc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(57, 57, 57)
                                .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnLamMoi2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnAddDis)
                                    .addComponent(btnEditDis, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8))
                            .addComponent(jScrollPane24))
                        .addContainerGap())))
        );
        DiscountLayout.setVerticalGroup(
            DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DiscountLayout.createSequentialGroup()
                .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DiscountLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnAddDis)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addComponent(btnEditDis)
                        .addGap(25, 25, 25)
                        .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLamMoi2)
                            .addComponent(txt_tim_sp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel181, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7))
                    .addGroup(DiscountLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtMa_chiet_khau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_so_tien_giam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel180)
                                .addComponent(jLabel106, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_code_chi_tiet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10))
                            .addComponent(jLabel105)
                            .addComponent(jdc_ngay_bat_dau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel184)
                            .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtMau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel151)
                                .addComponent(txtKieuDang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel152)
                                .addComponent(txtKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel85))
                            .addComponent(jdc_ngay_ket_thuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, Short.MAX_VALUE)))
                .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel182)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, DiscountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_tim_chuong_trình)
                        .addComponent(jLabel183, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );

        TabGiamGia.addTab("Giảm giá trực tiếp", Discount);

        javax.swing.GroupLayout jpViewQLKMLayout = new javax.swing.GroupLayout(jpViewQLKM);
        jpViewQLKM.setLayout(jpViewQLKMLayout);
        jpViewQLKMLayout.setHorizontalGroup(
            jpViewQLKMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpViewQLKMLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel76)
                .addGap(384, 384, 384))
            .addComponent(TabGiamGia)
        );
        jpViewQLKMLayout.setVerticalGroup(
            jpViewQLKMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpViewQLKMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel76)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TabGiamGia)
                .addContainerGap())
        );

        javax.swing.GroupLayout ViewHeThongLayout = new javax.swing.GroupLayout(ViewHeThong);
        ViewHeThong.setLayout(ViewHeThongLayout);
        ViewHeThongLayout.setHorizontalGroup(
            ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLHD, javax.swing.GroupLayout.DEFAULT_SIZE, 1045, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLBH2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLDM, javax.swing.GroupLayout.DEFAULT_SIZE, 1045, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLTK, javax.swing.GroupLayout.DEFAULT_SIZE, 1045, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewDoiMK, javax.swing.GroupLayout.DEFAULT_SIZE, 1045, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLSPP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLSPP2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLSPP3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLKH, javax.swing.GroupLayout.DEFAULT_SIZE, 1045, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewTK, javax.swing.GroupLayout.DEFAULT_SIZE, 1045, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ViewHeThongLayout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addComponent(jpViewQLKM, javax.swing.GroupLayout.DEFAULT_SIZE, 1030, Short.MAX_VALUE)
                    .addGap(8, 8, 8)))
        );
        ViewHeThongLayout.setVerticalGroup(
            ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 757, Short.MAX_VALUE)
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLHD, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLBH2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLDM, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLTK, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewDoiMK, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLSPP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLSPP2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLSPP3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewQLKH, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpViewTK, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE))
            .addGroup(ViewHeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ViewHeThongLayout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addComponent(jpViewQLKM, javax.swing.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE)
                    .addGap(26, 26, 26)))
        );

        javax.swing.GroupLayout jpContainerLayout = new javax.swing.GroupLayout(jpContainer);
        jpContainer.setLayout(jpContainerLayout);
        jpContainerLayout.setHorizontalGroup(
            jpContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpContainerLayout.createSequentialGroup()
                .addComponent(MenuHeThong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ViewHeThong, javax.swing.GroupLayout.PREFERRED_SIZE, 1029, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpContainerLayout.setVerticalGroup(
            jpContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MenuHeThong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(ViewHeThong, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpContainer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void jpQLBHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpQLBHMouseClicked
        this.jpViewQLHD.setVisible(false);
        this.jpViewQLDM.setVisible(false);
        this.jpViewQLKH.setVisible(false);
        this.jpViewQLKM.setVisible(false);
        this.jpViewQLSPP.setVisible(false);
        this.jpViewQLTK.setVisible(false);
        this.jpViewTK.setVisible(false);
        this.jpViewQLBH2.setVisible(true);     
        this.jpViewDoiMK.setVisible(false);
        this.jpViewQLSPP2.setVisible(false);
        this.jpViewQLSPP3.setVisible(false);     
        this.jpQLBH.setBackground(new Color(220, 220, 220));
        this.jpQLDM.setBackground(new Color(255,102,0));
        this.jpQLKH.setBackground(new Color(255,102,0));
        this.jpQLKM.setBackground(new Color(255,102,0));
        this.jpQLSP.setBackground(new Color(255,102,0));
        this.jpQLTK.setBackground(new Color(255,102,0));
        this.jpTK.setBackground(new Color(255,102,0));
        this.jpQLHD.setBackground(new Color(255,102,0));     

    }//GEN-LAST:event_jpQLBHMouseClicked

    private void jpQLSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpQLSPMouseClicked
        this.jpViewQLHD.setVisible(false);
        this.jpViewQLDM.setVisible(false);
        this.jpViewQLKH.setVisible(false);
        this.jpViewQLKM.setVisible(false);
        this.jpViewQLSPP.setVisible(true);
        this.jpViewQLTK.setVisible(false);
        this.jpViewTK.setVisible(false);
        this.jpViewQLBH2.setVisible(false);       
        this.jpViewDoiMK.setVisible(false); 
        this.jpViewQLSPP2.setVisible(false);
        this.jpViewQLSPP3.setVisible(false);                
        this.jpQLBH.setBackground(new Color(255,102,0));
        this.jpQLDM.setBackground(new Color(255,102,0));
        this.jpQLKH.setBackground(new Color(255,102,0));
        this.jpQLKM.setBackground(new Color(255,102,0));
        this.jpQLSP.setBackground(new Color(220, 220, 220));
        this.jpQLTK.setBackground(new Color(255,102,0));
        this.jpTK.setBackground(new Color(255,102,0));
        this.jpQLHD.setBackground(new Color(255,102,0));
        
        // Hòa
        if (this.jpViewQLSPP.isVisible()) {
            this.setEnableFormSP();
            this.setInputFields("sản phẩm");
            this.cbbLocTheoDM.setSelectedItem("");
            this.cbbTrangThaiSP.setSelectedItem("");
            this.cbbLocNhaCC.setSelectedItem("");
            currentPage = 1;
            updatePagination(qlsp.getAllProductDetail());

            currentPages = 1;
            updatePaginations(qlsp.getAllProduct());
        }
        
    }//GEN-LAST:event_jpQLSPMouseClicked

    private void jpQLDMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpQLDMMouseClicked
        this.jpViewQLHD.setVisible(false);
        this.jpViewQLDM.setVisible(true);
        this.jpViewQLKH.setVisible(false);
        this.jpViewQLKM.setVisible(false);
        this.jpViewQLSPP.setVisible(false);
        this.jpViewQLTK.setVisible(false);
        this.jpViewTK.setVisible(false);
        this.jpViewQLBH2.setVisible(false);      
        this.jpViewDoiMK.setVisible(false);
        this.jpViewQLSPP2.setVisible(false);
        this.jpViewQLSPP3.setVisible(false);      
        this.jpQLBH.setBackground(new Color(255,102,0));
        this.jpQLDM.setBackground(new Color(220, 220, 220));
        this.jpQLKH.setBackground(new Color(255,102,0));
        this.jpQLKM.setBackground(new Color(255,102,0));
        this.jpQLSP.setBackground(new Color(255,102,0));
        this.jpQLTK.setBackground(new Color(255,102,0));
        this.jpTK.setBackground(new Color(255,102,0));
        this.jpQLHD.setBackground(new Color(255,102,0));
    }//GEN-LAST:event_jpQLDMMouseClicked

    private void jpQLKHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpQLKHMouseClicked
        this.jpViewQLHD.setVisible(false);
        this.jpViewQLDM.setVisible(false);
        this.jpViewQLKH.setVisible(true);
        this.jpViewQLKM.setVisible(false);
        this.jpViewQLSPP.setVisible(false);
        this.jpViewQLTK.setVisible(false);
        this.jpViewTK.setVisible(false);
        this.jpViewQLBH2.setVisible(false);      
        this.jpViewDoiMK.setVisible(false);
        this.jpViewQLSPP2.setVisible(false);
        this.jpViewQLSPP3.setVisible(false);     
        this.jpQLBH.setBackground(new Color(255,102,0));
        this.jpQLDM.setBackground(new Color(255,102,0));
        this.jpQLKH.setBackground(new Color(220, 220, 220));
        this.jpQLKM.setBackground(new Color(255,102,0));
        this.jpQLSP.setBackground(new Color(255,102,0));
        this.jpQLTK.setBackground(new Color(255,102,0));
        this.jpTK.setBackground(new Color(255,102,0));
        this.jpQLHD.setBackground(new Color(255,102,0));
        
            //Hòa
            if(this.jpViewQLKH.isVisible()){
            this.setEnableFormKH();
            this.txtMaKH.setText("");
            this.txtITenKhachHang1.setText("");
            this.txtSoDiienThoai.setText("");
            this.txtDiemTichLuy.setText("");
            this.txtTinhDiem.setText("");
            this.txtDiemDaDung.setText("");
            this.tblKhachH.clearSelection();
        }
    }//GEN-LAST:event_jpQLKHMouseClicked

    private void jpTKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpTKMouseClicked
        this.jpViewQLHD.setVisible(false);
        this.jpViewQLDM.setVisible(false);
        this.jpViewQLKH.setVisible(false);
        this.jpViewQLKM.setVisible(false);
        this.jpViewQLSPP.setVisible(false);
        this.jpViewQLTK.setVisible(false);
        this.jpViewTK.setVisible(true);
        this.jpViewQLBH2.setVisible(false);      
        this.jpViewDoiMK.setVisible(false);
        this.jpViewQLSPP2.setVisible(false);
        this.jpViewQLSPP3.setVisible(false);         
        this.jpQLBH.setBackground(new Color(255,102,0));
        this.jpQLDM.setBackground(new Color(255,102,0));
        this.jpQLKH.setBackground(new Color(255,102,0));
        this.jpQLKM.setBackground(new Color(255,102,0));
        this.jpQLSP.setBackground(new Color(255,102,0));
        this.jpQLTK.setBackground(new Color(255,102,0));
        this.jpTK.setBackground(new Color(220, 220, 220));
        this.jpQLHD.setBackground(new Color(255,102,0));
    }//GEN-LAST:event_jpTKMouseClicked

    private void jpQLTKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpQLTKMouseClicked
 this.jpViewQLHD.setVisible(false);
    this.jpViewQLDM.setVisible(false);
    this.jpViewQLKH.setVisible(false);
    this.jpViewQLKM.setVisible(false);
    this.jpViewQLSPP.setVisible(false);
    this.jpViewQLTK.setVisible(true);
    this.jpViewTK.setVisible(false);
    this.jpViewQLBH2.setVisible(false);    
    this.jpViewDoiMK.setVisible(false);
    this.jpViewQLSPP2.setVisible(false);
    this.jpViewQLSPP3.setVisible(false); 
    this.jpQLBH.setBackground(new Color(255, 102, 0));
    this.jpQLDM.setBackground(new Color(255, 102, 0));
    this.jpQLKH.setBackground(new Color(255, 102, 0));
    this.jpQLKM.setBackground(new Color(255, 102, 0));
    this.jpQLSP.setBackground(new Color(255, 102, 0));
    this.jpQLTK.setBackground(new Color(220, 220, 220));
    this.jpTK.setBackground(new Color(255, 102, 0));
    this.jpQLHD.setBackground(new Color(255,102,0));   
    
    ql.loadUserInfo(currentUserId, txtHoTen, txtUsername, txtEmail, txtSdt, txtDiaChi, txtVaiTro);
    
    }//GEN-LAST:event_jpQLTKMouseClicked
 
    private void jpQLKMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpQLKMMouseClicked
        this.jpViewQLHD.setVisible(false);
        this.jpViewQLDM.setVisible(false);
        this.jpViewQLKH.setVisible(false);
        this.jpViewQLKM.setVisible(true);
        this.jpViewQLSPP.setVisible(false);
        this.jpViewQLTK.setVisible(false);
        this.jpViewTK.setVisible(false);
        this.jpViewQLBH2.setVisible(false);       
        this.jpViewDoiMK.setVisible(false);
        this.jpViewQLSPP2.setVisible(false);
        this.jpViewQLSPP3.setVisible(false);      
        this.jpQLBH.setBackground(new Color(255,102,0));
        this.jpQLDM.setBackground(new Color(255,102,0));
        this.jpQLKH.setBackground(new Color(255,102,0));
        this.jpQLKM.setBackground(new Color(220, 220, 220));
        this.jpQLSP.setBackground(new Color(255,102,0));
        this.jpQLTK.setBackground(new Color(255,102,0));
        this.jpTK.setBackground(new Color(255,102,0));
        this.jpQLHD.setBackground(new Color(255,102,0));
        
        // Đông
        try {

            jdc_ngay_bat_dau_voucher.setDate(null);
            jdc_ngay_ket_thuc_voucher.setDate(null);
            txt_so_luong_vou.setText("");
            txtMaGiamGia.setText("");
            txtCode.setText("");
            txtMoTa.setText("");
            txtPhanTramChietKhau.setText("");
            txtMa_chiet_khau.setText("");
            txt_tien_toi_da.setText("");
            txt_tien_ap_dung.setText("");
            txt_so_tien_giam.setText("");
            ProductDetails_Service pro = new ProductDetails_Service();
            PointsConversion_Service psv = new PointsConversion_Service();
            DiscountService dis = new DiscountService();
            VoucherService vou = new VoucherService();
           
            ProductDetails x = null;
           
        
            txtPoint1.setEditable(false);
            txtCode.setEditable(false);
            txtMaGiamGia.setEnabled(false);
            txtMoTa.setEnabled(false);
            txtCode.setEnabled(false);
            txtPhanTramChietKhau.setEnabled(false);
            jdc_ngay_bat_dau_voucher.setEnabled(false);
            jdc_ngay_ket_thuc_voucher.setEnabled(false);
            txt_tien_ap_dung.setEnabled(false);
            txt_tien_toi_da.setEnabled(false);
            txt_so_luong_vou.setEnabled(false);
            txt_so_tien_giam.setEnabled(false);
            
            txtMa_chiet_khau.setEnabled(false);
            txtPoint.setText(String.valueOf(1));
            double diemSangTien = psv.diemSangTien();
            double tienSangDiem = psv.tienSangDiem();
            txtMoney1.setText(String.valueOf(diemSangTien));
            txtMoney.setText(String.valueOf(tienSangDiem));
            txtPoint1.setText(String.valueOf(1000));
//            vou.loadTableVoucher(tblCoupon);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
    }//GEN-LAST:event_jpQLKMMouseClicked

    private void btnDangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangXuatActionPerformed
    View_Login loginView = new View_Login();   
    loginView.setVisible(true);  
    this.dispose();
    }//GEN-LAST:event_btnDangXuatActionPerformed
    
    private void txtTenSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenSanPhamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenSanPhamActionPerformed





private int getCustomerId(String tenKhachHang, String soDienThoai) throws SQLException {
    // Kiểm tra định dạng số điện thoại
    if (!soDienThoai.matches("^0[0-9]{9}$")) {
        throw new SQLException("Số điện thoại không hợp lệ! Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số.");
    }

    String queryCheck = "SELECT customer_id FROM Customers WHERE customer_name = ? AND phone = ?";
    String queryInsert = "INSERT INTO Customers (customer_name, phone, point_gained, points_used) VALUES (?, ?, 0, 0)";

    try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
         PreparedStatement pstCheck = connection.prepareStatement(queryCheck);
         PreparedStatement pstInsert = connection.prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS)) {

        pstCheck.setString(1, tenKhachHang);
        pstCheck.setString(2, soDienThoai);

        try (ResultSet rs = pstCheck.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                pstInsert.setString(1, tenKhachHang);
                pstInsert.setString(2, soDienThoai);
                pstInsert.executeUpdate();

                try (ResultSet generatedKeys = pstInsert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Thêm khách hàng thất bại, không có ID được tạo.");
                    }
                }
            }
        }
    }
}


private int getProductDetailsId(String productDetailsCode) throws SQLException {
    String query = "SELECT productDetails_id FROM ProductDetails WHERE productDetails_code = ?";
    try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
         PreparedStatement pst = connection.prepareStatement(query)) {

        pst.setString(1, productDetailsCode);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("productDetails_id");
            } else {
                throw new SQLException("Không tìm thấy chi tiết sản phẩm cho mã sản phẩm chi tiết: " + productDetailsCode);
            }
        }
    }
}

    private void resetInvoiceData() {
        txtNhapSoLuong1.setText("");
    txtTenKhachHang.setText("");
    txtSDTKhachHang.setText("");
    txtSoDiemDaDung.setText("0");
    lblTienTruocKhiGiam.setText("0");
    DefaultTableModel model = (DefaultTableModel) tblDSSPtrongHD.getModel();
    model.setRowCount(0);
    lblTongTienThanhToan.setText("0.00 VNĐ");
   lblDiemConLai.setText("");
//    ql.DanhSachHD(tblDSHD);
tblDSHD.setModel(qlHoaDon.loadOrderData());
    cbbVouchers.setSelectedIndex(0);
}
    private void xoaHDCkhiThanhToan() {
    int selectedRow = tblHoaDonCho.getSelectedRow();
    if (selectedRow != -1) {
        DefaultTableModel model = (DefaultTableModel) tblHoaDonCho.getModel();
        model.removeRow(selectedRow); // Xóa dòng được chọn
    }
}
    
     private double getVoucherDiscountPercent(String voucherCode) throws SQLException {
    String query = "SELECT discount_value FROM Voucher WHERE voucher_code = ?";
    try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
         PreparedStatement pst = connection.prepareStatement(query)) {

        pst.setString(1, voucherCode);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("discount_value"); // Giả sử discount_value là phần trăm giảm giá
            } else {
                throw new SQLException("Không tìm thấy voucher cho mã: " + voucherCode);
            }
        }
    }
}
     
  
    private String getEmployeeNameById(int userId) {
        String hoTen = null;
        try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
             PreparedStatement pstGetEmployeeName = connection.prepareStatement("SELECT hoTen FROM Users WHERE user_id = ?")) {
            pstGetEmployeeName.setInt(1, userId);
            ResultSet rs = pstGetEmployeeName.executeQuery();
            if (rs.next()) {
                hoTen = rs.getString("hoTen");
            } else {
                System.out.println("No employee name found for userId: " + userId);  // Debug line
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy họ tên nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return hoTen;
    }
    
    private void insertOrderDetails(long maHoaDon) throws SQLException {
    String queryInsertOrderDetails = "INSERT INTO OrderDetails (order_id, quantity, unit_price, productDetails_id) VALUES (?, ?, ?, ?)";
    String queryUpdateProductDetails = "UPDATE ProductDetails SET quantity = quantity - ? WHERE productDetails_id = ?";

    try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
         PreparedStatement pstInsertOrderDetails = connection.prepareStatement(queryInsertOrderDetails);
         PreparedStatement pstUpdateProductDetails = connection.prepareStatement(queryUpdateProductDetails)) {

        for (int i = 0; i < tblDSSPtrongHD.getRowCount(); i++) {
            try {
                String productDetailsCode = tblDSSPtrongHD.getValueAt(i, 1).toString();
                int productDetailsId = getProductDetailsId(productDetailsCode); 
                int soLuong = Integer.parseInt(tblDSSPtrongHD.getValueAt(i, 7).toString()); 
                double gia = Double.parseDouble(tblDSSPtrongHD.getValueAt(i, 8).toString().replace(" VNĐ", "").replace(",", ".")); // Giả định cột giá bán là cột thứ 7

                // Thêm chi tiết đơn hàng
                pstInsertOrderDetails.setLong(1, maHoaDon);
                pstInsertOrderDetails.setInt(2, soLuong);
                pstInsertOrderDetails.setDouble(3, gia);
                pstInsertOrderDetails.setInt(4, productDetailsId);
                pstInsertOrderDetails.addBatch();

//                 Cập nhật số lượng sản phẩm chi tiết
                pstUpdateProductDetails.setInt(1, soLuong);
                pstUpdateProductDetails.setInt(2, productDetailsId);
                pstUpdateProductDetails.addBatch();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi định dạng số tại dòng " + (i + 1) + ": " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        pstInsertOrderDetails.executeBatch();
        pstUpdateProductDetails.executeBatch();
    }
}
    
    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed

//   try {
//        // Kiểm tra xem có hóa đơn chờ được chọn hay không
//        int selectedRow = tblHoaDonCho.getSelectedRow();
//
//        String tenKhachHang = txtTenKhachHang.getText().trim();
//        String soDienThoai = txtSDTKhachHang.getText().trim();
//        int diemDaDung = Integer.parseInt(txtSoDiemDaDung.getText().trim().isEmpty() ? "0" : txtSoDiemDaDung.getText().trim());
//        String phuongThucThanhToan = rdoTienMat.isSelected() ? "Tiền mặt" : "Chuyển khoản";
//        String voucherCode = (String) cbbVouchers.getSelectedItem();
//
//        // Lấy thông tin giảm giá từ voucher
//        double voucherDiscountPercent = 0;
//        double maximumReduce = 0;
//        double moneyApplies = 0;
//
//        if (voucherCode != null && !voucherCode.isEmpty()) {
//            voucherDiscountPercent = getVoucherDiscountPercent(voucherCode);
//            // Lấy thêm các thông tin cần thiết từ cơ sở dữ liệu
//            String query = "SELECT money_applies, maximum_reduce FROM Voucher WHERE voucher_code = ?";
//            try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
//                 PreparedStatement pst = connection.prepareStatement(query)) {
//                pst.setString(1, voucherCode);
//                ResultSet rs = pst.executeQuery();
//                if (rs.next()) {
//                    moneyApplies = rs.getDouble("money_applies");
//                    maximumReduce = rs.getDouble("maximum_reduce");
//                }
//            }
//        }
//
//        // Tính toán tổng tiền
//        double tongTienChuaVAT = 0;
//        double VAT = 0.08; // 8%
//        for (int i = 0; i < tblDSSPtrongHD.getRowCount(); i++) {
//            int soLuong = Integer.parseInt(tblDSSPtrongHD.getValueAt(i, 7).toString());
//            String giaBanString = tblDSSPtrongHD.getValueAt(i, 8).toString().replace(" VNĐ", "").replace(",", ".");
//            double giaBan = Double.parseDouble(giaBanString);
//            tongTienChuaVAT += soLuong * giaBan;
//        }
//
//        double tienVAT = tongTienChuaVAT * VAT;
//        double tongTienCoVAT = tongTienChuaVAT + tienVAT;
//
//        // Tính toán giảm giá theo voucher
//        double tongTienSauVoucher = tongTienCoVAT; // Khởi tạo tổng tiền sau khi giảm
//
//        // Kiểm tra điều kiện giảm giá
//        if (tongTienCoVAT >= moneyApplies) {
//            double discountAmount = tongTienCoVAT * (voucherDiscountPercent / 100);
//            // Giảm tối đa theo cột maximum_reduce
//            if (discountAmount > maximumReduce) {
//                discountAmount = maximumReduce;
//            }
//            tongTienSauVoucher -= discountAmount; // Áp dụng giảm giá
//        } else {
//            // Không đủ điều kiện để áp dụng voucher
//            voucherCode = null;
//        }
//
//        lblTongTienThanhToan.setText(String.format("%.2f VNĐ", tongTienSauVoucher));
//
//        // Kiểm tra thông tin khách hàng
//        if (tenKhachHang.isEmpty() || soDienThoai.isEmpty()) {
//            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin khách hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        if (tblDSSPtrongHD.getRowCount() == 0) {
//            JOptionPane.showMessageDialog(null, "Giỏ hàng chưa có sản phẩm.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//            return;
//        }
//
//        // Lấy tên nhân viên
//        String tenNhanVien = getEmployeeNameById(this.currentUserId);
//        lblNhanVienBan.setText(tenNhanVien != null ? tenNhanVien : "Không tìm thấy tên nhân viên");
//
//        // Nếu có hóa đơn chờ được chọn
//        if (selectedRow != -1) {
//            String maHoaDon = tblHoaDonCho.getValueAt(selectedRow, 1).toString();
//
//            // Kiểm tra hóa đơn trong cơ sở dữ liệu
//            String checkInvoiceQuery = "SELECT status FROM Orders WHERE order_code = ?";
//            try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
//                 PreparedStatement pstCheckInvoice = connection.prepareStatement(checkInvoiceQuery)) {
//
//                pstCheckInvoice.setString(1, maHoaDon);
//                ResultSet rs = pstCheckInvoice.executeQuery();
//
//                if (rs.next()) {
//                    String currentStatus = rs.getString("status");
//
//                    if ("Chưa thanh toán".equals(currentStatus)) {
//                        // Cập nhật số lượng kho trước khi cập nhật trạng thái hóa đơn
//                        capNhatSoLuongKho(connection, maHoaDon);
//
//                        // Cập nhật hóa đơn đã thanh toán
//                        String updateInvoiceQuery = "UPDATE Orders SET status = N'Đã thanh toán', total_price = ?, payment_method = ?, voucher_id = (SELECT voucher_id FROM Voucher WHERE voucher_code = ?) WHERE order_code = ?";
//                        try (PreparedStatement pstUpdateInvoice = connection.prepareStatement(updateInvoiceQuery)) {
//                            pstUpdateInvoice.setDouble(1, tongTienSauVoucher);
//                            pstUpdateInvoice.setString(2, phuongThucThanhToan);
//                            if (voucherCode != null) {
//                                pstUpdateInvoice.setString(3, voucherCode);
//                            } else {
//                                pstUpdateInvoice.setNull(3, Types.VARCHAR);
//                            }
//                            pstUpdateInvoice.setString(4, maHoaDon);
//                            pstUpdateInvoice.executeUpdate();
//                        }
//
//                        // Cập nhật chi tiết hóa đơn
//                        CapNhatHDCT(connection, maHoaDon);
//                        
//                        // Cập nhật số lượng voucher
//                        if (voucherCode != null && !voucherCode.isEmpty()) {
//                            String updateVoucherQuery = "UPDATE Voucher SET quantity_voucher = quantity_voucher - 1 WHERE voucher_code = ?";
//                            try (PreparedStatement pstUpdateVoucher = connection.prepareStatement(updateVoucherQuery)) {
//                                pstUpdateVoucher.setString(1, voucherCode);
//                                pstUpdateVoucher.executeUpdate();
//                            }
//                        }
//
//                        resetInvoiceData();
//
//                        JOptionPane.showMessageDialog(null, "Hóa đơn đã được cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                        xoaHDCkhiThanhToan();
//                        isContinuingPayment = false; 
//                        tblDSHD.setModel(qlHoaDon.loadOrderData());
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Hóa đơn đã được thanh toán trước đó!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                    }
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi kiểm tra hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//        } else {
//            // Nếu không có hóa đơn chờ, tạo hóa đơn mới
//            String queryHoaDon = "INSERT INTO Orders (user_id, order_date, status, customer_id, voucher_id, VAT, payment_method, total_price, last_update_date_point) VALUES (?, ?, ?, ?, (SELECT voucher_id FROM Voucher WHERE voucher_code = ?), ?, ?, ?, ?)";
//
//            try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
//                 PreparedStatement pstHoaDon = connection.prepareStatement(queryHoaDon, Statement.RETURN_GENERATED_KEYS)) {
//
//                int customerId = getCustomerId(tenKhachHang, soDienThoai);
//
//                pstHoaDon.setInt(1, this.currentUserId);
//                pstHoaDon.setDate(2, new java.sql.Date(System.currentTimeMillis()));
//                pstHoaDon.setString(3, "Đã thanh toán");
//                pstHoaDon.setInt(4, customerId);
//                if (voucherCode != null) {
//                    pstHoaDon.setString(5, voucherCode);
//                } else {
//                    pstHoaDon.setNull(5, Types.VARCHAR);
//                }
//                pstHoaDon.setDouble(6, VAT * 100);
//                pstHoaDon.setString(7, phuongThucThanhToan);
//                pstHoaDon.setDouble(8, tongTienSauVoucher);
//                pstHoaDon.setDate(9, new java.sql.Date(System.currentTimeMillis()));
//
//                pstHoaDon.executeUpdate();
//                try (ResultSet generatedKeys = pstHoaDon.getGeneratedKeys()) {
//                    if (generatedKeys.next()) {
//                        long maHoaDonMoi = generatedKeys.getLong(1);
//                        insertOrderDetails(maHoaDonMoi); // Chèn chi tiết đơn hàng mới
//                    } else {
//                        throw new SQLException("Tạo hóa đơn thất bại, không có ID được tạo.");
//                    }
//                }
//
//                // Cập nhật số lượng voucher
//                if (voucherCode != null && !voucherCode.isEmpty()) {
//                    String updateVoucherQuery = "UPDATE Voucher SET quantity_voucher = quantity_voucher - 1 WHERE voucher_code = ?";
//                    try (PreparedStatement pstUpdateVoucher = connection.prepareStatement(updateVoucherQuery)) {
//                        pstUpdateVoucher.setString(1, voucherCode);
//                        pstUpdateVoucher.executeUpdate();
//                    }
//                }
//
//                resetInvoiceData();
//
//                JOptionPane.showMessageDialog(null, "Thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                xoaHDCkhiThanhToan();
//                tblDSHD.setModel(qlHoaDon.loadOrderData());
//                isContinuingPayment = false; 
//            } catch (SQLException e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi tạo hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    } catch (NumberFormatException ex) {
//        JOptionPane.showMessageDialog(null, "Vui lòng nhập số điểm đã dùng hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
//    }   catch (SQLException ex) {
//            Logger.getLogger(view_TrangChu.class.getName()).log(Level.SEVERE, null, ex);
//        }

  try {
        // Kiểm tra xem có hóa đơn chờ được chọn hay không
        int selectedRow = tblHoaDonCho.getSelectedRow();

        String tenKhachHang = txtTenKhachHang.getText().trim();
        String soDienThoai = txtSDTKhachHang.getText().trim();
        int diemDaDung = Integer.parseInt(txtSoDiemDaDung.getText().trim().isEmpty() ? "0" : txtSoDiemDaDung.getText().trim());
        String phuongThucThanhToan = rdoTienMat.isSelected() ? "Tiền mặt" : "Chuyển khoản";
        String voucherCode = (String) cbbVouchers.getSelectedItem();

        // Lấy thông tin giảm giá từ voucher
        double voucherDiscountPercent = 0;
        double maximumReduce = 0;
        double moneyApplies = 0;

        if (voucherCode != null && !voucherCode.isEmpty()) {
            voucherDiscountPercent = getVoucherDiscountPercent(voucherCode);
            // Lấy thêm các thông tin cần thiết từ cơ sở dữ liệu
            String query = "SELECT money_applies, maximum_reduce FROM Voucher WHERE voucher_code = ?";
            try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
                 PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setString(1, voucherCode);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    moneyApplies = rs.getDouble("money_applies");
                    maximumReduce = rs.getDouble("maximum_reduce");
                }
            }
        }

        // Tính toán tổng tiền
        double tongTienChuaVAT = 0;
        double VAT = 0.08; // 8%
        for (int i = 0; i < tblDSSPtrongHD.getRowCount(); i++) {
            int soLuong = Integer.parseInt(tblDSSPtrongHD.getValueAt(i, 7).toString());
            String giaBanString = tblDSSPtrongHD.getValueAt(i, 8).toString().replace(" VNĐ", "").replace(",", ".");
            double giaBan = Double.parseDouble(giaBanString);
            tongTienChuaVAT += soLuong * giaBan;
        }

        double tienVAT = tongTienChuaVAT * VAT;
        double tongTienCoVAT = tongTienChuaVAT + tienVAT;

        // Tính toán giảm giá theo voucher
        double tongTienSauVoucher = tongTienCoVAT;

        if (tongTienCoVAT >= moneyApplies) {
            double discountAmount = tongTienCoVAT * (voucherDiscountPercent / 100);
            if (discountAmount > maximumReduce) {
                discountAmount = maximumReduce;
            }
            tongTienSauVoucher -= discountAmount;
        } else {
            voucherCode = null;
        }

        lblTongTienThanhToan.setText(String.format("%.2f VNĐ", tongTienSauVoucher));

        // Kiểm tra thông tin khách hàng
        if (tenKhachHang.isEmpty() || soDienThoai.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin khách hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (tblDSSPtrongHD.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Giỏ hàng chưa có sản phẩm.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Lấy tên nhân viên
        String tenNhanVien = getEmployeeNameById(this.currentUserId);
        lblNhanVienBan.setText(tenNhanVien != null ? tenNhanVien : "Không tìm thấy tên nhân viên");

        // Lấy thông tin quy đổi điểm từ bảng PointsConversion
        double moneyIntoPoints = getMoneyIntoPoints();
        // Tính số điểm sẽ tích lũy dựa trên tổng tiền hóa đơn
        int soDiemTichLuy = (int) (tongTienCoVAT / moneyIntoPoints);

        // Nếu có hóa đơn chờ được chọn
        if (selectedRow != -1) {
            String maHoaDon = tblHoaDonCho.getValueAt(selectedRow, 1).toString();

            // Kiểm tra hóa đơn trong cơ sở dữ liệu
            String checkInvoiceQuery = "SELECT status FROM Orders WHERE order_code = ?";
            try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
                 PreparedStatement pstCheckInvoice = connection.prepareStatement(checkInvoiceQuery)) {

                pstCheckInvoice.setString(1, maHoaDon);
                ResultSet rs = pstCheckInvoice.executeQuery();

                if (rs.next()) {
                    String currentStatus = rs.getString("status");

                    if ("Chưa thanh toán".equals(currentStatus)) {
                        // Cập nhật số lượng kho trước khi cập nhật trạng thái hóa đơn
                        capNhatSoLuongKho(connection, maHoaDon);

                        // Cập nhật hóa đơn đã thanh toán
                        String updateInvoiceQuery = "UPDATE Orders SET status = N'Đã thanh toán', total_price = ?, payment_method = ?, voucher_id = (SELECT voucher_id FROM Voucher WHERE voucher_code = ?) WHERE order_code = ?";
                        try (PreparedStatement pstUpdateInvoice = connection.prepareStatement(updateInvoiceQuery)) {
                            pstUpdateInvoice.setDouble(1, tongTienSauVoucher);
                            pstUpdateInvoice.setString(2, phuongThucThanhToan);
                            if (voucherCode != null) {
                                pstUpdateInvoice.setString(3, voucherCode);
                            } else {
                                pstUpdateInvoice.setNull(3, Types.VARCHAR);
                            }
                            pstUpdateInvoice.setString(4, maHoaDon);
                            pstUpdateInvoice.executeUpdate();
                        }

                        // Cập nhật chi tiết hóa đơn
                        CapNhatHDCT(connection, maHoaDon);
                        
                        // Cập nhật số lượng voucher
                        if (voucherCode != null && !voucherCode.isEmpty()) {
                            String updateVoucherQuery = "UPDATE Voucher SET quantity_voucher = quantity_voucher - 1 WHERE voucher_code = ?";
                            try (PreparedStatement pstUpdateVoucher = connection.prepareStatement(updateVoucherQuery)) {
                                pstUpdateVoucher.setString(1, voucherCode);
                                pstUpdateVoucher.executeUpdate();
                            }
                        }

                        // Cập nhật điểm tích lũy cho khách hàng
                        int idKhachHang = getCustomerId(tenKhachHang, soDienThoai);
                        updateCustomerPoints(connection, idKhachHang, soDiemTichLuy);

                        resetInvoiceData();

                        JOptionPane.showMessageDialog(null, "Hóa đơn đã được cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        xoaHDCkhiThanhToan();
                        isContinuingPayment = false; 
                        tblDSHD.setModel(qlHoaDon.loadOrderData());
                    } else {
                        JOptionPane.showMessageDialog(null, "Hóa đơn đã được thanh toán trước đó!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi kiểm tra hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Nếu không có hóa đơn chờ, tạo hóa đơn mới
            String queryHoaDon = "INSERT INTO Orders (user_id, order_date, status, customer_id, voucher_id, VAT, payment_method, total_price, last_update_date_point) VALUES (?, ?, ?, ?, (SELECT voucher_id FROM Voucher WHERE voucher_code = ?), ?, ?, ?, ?)";

            try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
                 PreparedStatement pstHoaDon = connection.prepareStatement(queryHoaDon, Statement.RETURN_GENERATED_KEYS)) {

                int customerId = getCustomerId(tenKhachHang, soDienThoai);

                pstHoaDon.setInt(1, this.currentUserId);
                pstHoaDon.setDate(2, new java.sql.Date(System.currentTimeMillis()));
                pstHoaDon.setString(3, "Đã thanh toán");
                pstHoaDon.setInt(4, customerId);
                if (voucherCode != null) {
                    pstHoaDon.setString(5, voucherCode);
                } else {
                    pstHoaDon.setNull(5, Types.VARCHAR);
                }
                pstHoaDon.setDouble(6, VAT * 100);
                pstHoaDon.setString(7, phuongThucThanhToan);
                pstHoaDon.setDouble(8, tongTienSauVoucher);
                pstHoaDon.setDate(9, new java.sql.Date(System.currentTimeMillis()));

                pstHoaDon.executeUpdate();
                try (ResultSet generatedKeys = pstHoaDon.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long maHoaDonMoi = generatedKeys.getLong(1);
                        insertOrderDetails(maHoaDonMoi); // Chèn chi tiết đơn hàng mới
                    } else {
                        throw new SQLException("Tạo hóa đơn thất bại, không có ID được tạo.");
                    }
                }

                // Cập nhật số lượng voucher
                if (voucherCode != null && !voucherCode.isEmpty()) {
                    String updateVoucherQuery = "UPDATE Voucher SET quantity_voucher = quantity_voucher - 1 WHERE voucher_code = ?";
                    try (PreparedStatement pstUpdateVoucher = connection.prepareStatement(updateVoucherQuery)) {
                        pstUpdateVoucher.setString(1, voucherCode);
                        pstUpdateVoucher.executeUpdate();
                    }
                }

                // Cập nhật điểm tích lũy cho khách hàng
                int idKhachHang = getCustomerId(tenKhachHang, soDienThoai);
                updateCustomerPoints(connection, idKhachHang, soDiemTichLuy);

                resetInvoiceData();

                JOptionPane.showMessageDialog(null, "Hóa đơn đã được tạo và thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                tblDSHD.setModel(qlHoaDon.loadOrderData());
                xoaHDCkhiThanhToan();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi tạo hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
   
    }//GEN-LAST:event_btnThanhToanActionPerformed
 
    private double getMoneyIntoPoints() throws SQLException {
    double moneyIntoPoints = 0;
    String query = "SELECT money_into_points FROM PointsConversion";
    try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
         PreparedStatement pst = connection.prepareStatement(query);
         ResultSet rs = pst.executeQuery()) {
        if (rs.next()) {
            moneyIntoPoints = rs.getDouble("money_into_points");
        }
    }
    return moneyIntoPoints;
}

    private void updateCustomerPoints(Connection connection, int customerId, int pointsToAdd) throws SQLException {
    String query = "UPDATE Customers SET points = points + ? WHERE customer_id = ?";
    try (PreparedStatement pst = connection.prepareStatement(query)) {
        pst.setInt(1, pointsToAdd);
        pst.setInt(2, customerId);
        pst.executeUpdate();
    }
}

    
//    private void insertOrderDetails(long orderId) throws SQLException {
//    String query = "INSERT INTO OrderDetails (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
//    try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
//         PreparedStatement pst = connection.prepareStatement(query)) {
//
//        for (int i = 0; i < tblDSSPtrongHD.getRowCount(); i++) {
//            long productId = Long.parseLong(tblDSSPtrongHD.getValueAt(i, 0).toString());
//            int quantity = Integer.parseInt(tblDSSPtrongHD.getValueAt(i, 7).toString());
//            double unitPrice = Double.parseDouble(tblDSSPtrongHD.getValueAt(i, 8).toString().replace(" VNĐ", "").replace(",", "."));
//
//            pst.setLong(1, orderId);
//            pst.setLong(2, productId);
//            pst.setInt(3, quantity);
//            pst.setDouble(4, unitPrice);
//            pst.addBatch();
//        }
//        pst.executeBatch();
//    }
//}

    
   private void inSertHoaDonChiTiet(long orderId) throws SQLException {
    // Câu lệnh để lấy productDetails_id từ productDetails_code
    String getProductIdQuery = "SELECT productDetails_id FROM ProductDetails WHERE productDetails_code = ?";
    // Câu lệnh để chèn chi tiết hóa đơn
    String insertOrderDetailsQuery = "INSERT INTO OrderDetails (order_id, productDetails_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

    try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
         PreparedStatement pstGetProductId = connection.prepareStatement(getProductIdQuery);
         PreparedStatement pstInsertOrderDetails = connection.prepareStatement(insertOrderDetailsQuery)) {

        // Lặp qua tất cả các sản phẩm trong bảng tblDSSPtrongHD
        for (int i = 0; i < tblDSSPtrongHD.getRowCount(); i++) {
            String productDetailsCode = tblDSSPtrongHD.getValueAt(i, 1).toString(); // Giả sử mã sản phẩm nằm ở cột thứ 1
            int quantity = Integer.parseInt(tblDSSPtrongHD.getValueAt(i, 7).toString());
            String priceString = tblDSSPtrongHD.getValueAt(i, 8).toString().replace(" VNĐ", "").replace(",", ".");
            double price = Double.parseDouble(priceString);

            // Lấy productDetails_id từ productDetails_code
            pstGetProductId.setString(1, productDetailsCode);
            ResultSet rs = pstGetProductId.executeQuery();
            if (rs.next()) {
                int productDetailsId = rs.getInt("productDetails_id");

                // Thêm thông tin chi tiết đơn hàng vào cơ sở dữ liệu
                pstInsertOrderDetails.setLong(1, orderId);
                pstInsertOrderDetails.setInt(2, productDetailsId);
                pstInsertOrderDetails.setInt(3, quantity);
                pstInsertOrderDetails.setDouble(4, price);
                pstInsertOrderDetails.addBatch();
            } else {
                // Nếu không tìm thấy productDetails_id, ghi nhận lỗi hoặc xử lý khác
                System.err.println("Không tìm thấy productDetails_id cho mã sản phẩm: " + productDetailsCode);
            }
        }

        // Thực thi tất cả các câu lệnh chèn
        pstInsertOrderDetails.executeBatch();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new SQLException("Đã xảy ra lỗi khi chèn chi tiết hóa đơn: " + e.getMessage());
    }
}
     
   private void capNhatSoLuongKho(Connection connection, String maHoaDon) throws SQLException {
    // Lấy số lượng sản phẩm cũ từ hóa đơn
    String getOrderDetailsQuery = "SELECT productDetails_id, quantity FROM OrderDetails WHERE order_id = (SELECT order_id FROM Orders WHERE order_code = ?)";
    Map<Integer, Integer> oldQuantities = new HashMap<>();

    try (PreparedStatement pstGetOrderDetails = connection.prepareStatement(getOrderDetailsQuery)) {
        pstGetOrderDetails.setString(1, maHoaDon);
        ResultSet rs = pstGetOrderDetails.executeQuery();
        while (rs.next()) {
            int productDetailsId = rs.getInt("productDetails_id");
            int quantity = rs.getInt("quantity");
            oldQuantities.put(productDetailsId, quantity);
        }
    }

    // Tạo ánh xạ từ productDetails_code đến productDetails_id
    String getProductIdQuery = "SELECT productDetails_id FROM ProductDetails WHERE productDetails_code = ?";
    Map<String, Integer> productCodeToId = new HashMap<>();
    
    for (int i = 0; i < tblDSSPtrongHD.getRowCount(); i++) {
        String productDetailsCode = tblDSSPtrongHD.getValueAt(i, 1).toString();
        if (!productCodeToId.containsKey(productDetailsCode)) {
            try (PreparedStatement pstGetProductId = connection.prepareStatement(getProductIdQuery)) {
                pstGetProductId.setString(1, productDetailsCode);
                ResultSet rs = pstGetProductId.executeQuery();
                if (rs.next()) {
                    int productDetailsId = rs.getInt("productDetails_id");
                    productCodeToId.put(productDetailsCode, productDetailsId);
                }
            }
        }
    }

    // Cập nhật số lượng kho cho sản phẩm trong hóa đơn chờ
    String updateInventoryQuery = "UPDATE ProductDetails SET quantity = quantity + ? WHERE productDetails_id = ?";
    try (PreparedStatement pstUpdateInventory = connection.prepareStatement(updateInventoryQuery)) {
        for (Map.Entry<Integer, Integer> entry : oldQuantities.entrySet()) {
            int productDetailsId = entry.getKey();
            int oldQuantity = entry.getValue();
            
            // Kiểm tra nếu sản phẩm này vẫn còn trong giỏ hàng
            String productDetailsCode = null;
            for (int i = 0; i < tblDSSPtrongHD.getRowCount(); i++) {
                String code = tblDSSPtrongHD.getValueAt(i, 1).toString();
                if (productCodeToId.containsKey(code) && productCodeToId.get(code) == productDetailsId) {
                    productDetailsCode = code;
                    break;
                }
            }

            // Nếu không tìm thấy trong giỏ hàng, cộng lại số lượng vào kho
            if (productDetailsCode == null) {
                pstUpdateInventory.setInt(1, oldQuantity); // Cộng lại số lượng cũ vào kho
                pstUpdateInventory.setInt(2, productDetailsId);
                pstUpdateInventory.executeUpdate();
            }
        }
        
        // Cập nhật số lượng kho cho sản phẩm mới thêm vào giỏ
        for (int i = 0; i < tblDSSPtrongHD.getRowCount(); i++) {
            String productDetailsCode = tblDSSPtrongHD.getValueAt(i, 1).toString();
            String newQuantityStr = tblDSSPtrongHD.getValueAt(i, 7).toString();
            
            try {
                int newQuantity = Integer.parseInt(newQuantityStr);
                int productDetailsId = productCodeToId.getOrDefault(productDetailsCode, -1);
                
                if (productDetailsId != -1) {
                    int oldQuantity = oldQuantities.getOrDefault(productDetailsId, 0);
                    int quantityDifference = oldQuantity - newQuantity;
                    
                    if (quantityDifference != 0) {
                        pstUpdateInventory.setInt(1, quantityDifference); // Gia tăng nếu quantityDifference > 0, giảm nếu < 0
                        pstUpdateInventory.setInt(2, productDetailsId);
                        pstUpdateInventory.executeUpdate();
                    }
                }
            } catch (NumberFormatException e) {
                // Xử lý lỗi nếu giá trị không phải là số
                JOptionPane.showMessageDialog(null, "Có lỗi trong dữ liệu sản phẩm: " + e.getMessage(), "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

    
    private void CapNhatHDCT(Connection connection, String maHoaDon) throws SQLException {
    // Cập nhật chi tiết hóa đơn
    String updateOrderDetailsQuery = "DELETE FROM OrderDetails WHERE order_id = (SELECT order_id FROM Orders WHERE order_code = ?)";
    try (PreparedStatement pstUpdateOrderDetails = connection.prepareStatement(updateOrderDetailsQuery)) {
        pstUpdateOrderDetails.setString(1, maHoaDon);
        pstUpdateOrderDetails.executeUpdate();

        inSertHoaDonChiTiet(getOrderIdFromOrderCode(connection, maHoaDon));
    }

}
    
    private long getOrderIdFromOrderCode(Connection connection, String maHoaDon) throws SQLException {
    String query = "SELECT order_id FROM Orders WHERE order_code = ?";
    try (PreparedStatement pst = connection.prepareStatement(query)) {
        pst.setString(1, maHoaDon);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return rs.getLong("order_id");
        } else {
            throw new SQLException("Không tìm thấy mã đơn hàng.");
        }
    }
}
    
      
   private void cbbVouchersActionPerformed(java.awt.event.ActionEvent evt){
    CapNhatGiaKhiChonVoucher();
} 

   
   private void CapNhatGiaKhiChonVoucher() {
    try {
        String voucherCode = (String) cbbVouchers.getSelectedItem();
        double voucherDiscountPercent = 0;
        double maximumReduce = 0;
        double moneyApplies = 0;
        
        // Lấy thông tin giảm giá từ voucher
        if (voucherCode != null && !voucherCode.isEmpty()) {
            voucherDiscountPercent = getVoucherDiscountPercent(voucherCode);
            // Lấy thêm các thông tin cần thiết từ cơ sở dữ liệu
            String query = "SELECT money_applies, maximum_reduce FROM Voucher WHERE voucher_code = ?";
            try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
                 PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setString(1, voucherCode);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    moneyApplies = rs.getDouble("money_applies");
                    maximumReduce = rs.getDouble("maximum_reduce");
                }
            }
        }

        // Tính toán tổng tiền
        double tongTienChuaVAT = 0;
        double VAT = 0.08;

        for (int i = 0; i < tblDSSPtrongHD.getRowCount(); i++) {
            int soLuong = Integer.parseInt(tblDSSPtrongHD.getValueAt(i, 7).toString());
            String giaBanString = tblDSSPtrongHD.getValueAt(i, 8).toString().replace(" VNĐ", "").replace(",", ".");
            double giaBan = Double.parseDouble(giaBanString);
            tongTienChuaVAT += soLuong * giaBan;
        }

        double tienVAT = tongTienChuaVAT * VAT;
        double tongTienCoVAT = tongTienChuaVAT + tienVAT;

        double tongTienSauVoucher = tongTienCoVAT; // Khởi tạo tổng tiền sau khi giảm

        // Kiểm tra điều kiện giảm giá
        if (tongTienCoVAT >= moneyApplies) {
            double discountAmount = tongTienCoVAT * (voucherDiscountPercent / 100);
            // Giảm tối đa theo cột maximum_reduce
            if (discountAmount > maximumReduce) {
                discountAmount = maximumReduce;
            }
            tongTienSauVoucher -= discountAmount; // Áp dụng giảm giá
        }

        lblTongTienThanhToan.setText(String.format("%.2f VNĐ", tongTienSauVoucher));
    } catch (SQLException ex) {
        Logger.getLogger(View_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, "Lỗi khi tính tổng tiền: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}


        
private String maSP;
private String tenSP;
private String loaiSP;
private String mauSac;
private String kichThuoc;
private String phongCach;
private double giaBan;
    private void tblDSSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSSanPhamMouseClicked
//ql.clickVaoBang(tblDSSanPham, txtMaSPCT, txtTenSanPham, cbbLoaiCuaSP, cbbMauSacc, cbbKichThuocc, cbbPhongCachh);
// int selectedRow = tblDSSanPham.getSelectedRow();
//
//        // Kiểm tra xem có dòng nào được chọn không
//        if (selectedRow >= 0) {
//            // Lấy dữ liệu từ bảng và đẩy lên các thành phần giao diện
//            String stt = tblDSSanPham.getValueAt(selectedRow, 0).toString();
//            String maSPCT = tblDSSanPham.getValueAt(selectedRow, 1).toString();
//            String tenSanPham = tblDSSanPham.getValueAt(selectedRow, 2).toString();
//            String loaiCuaSP = tblDSSanPham.getValueAt(selectedRow, 3).toString();
//            String mauSac = tblDSSanPham.getValueAt(selectedRow, 4).toString();
//            String kichThuoc = tblDSSanPham.getValueAt(selectedRow, 5).toString();
//            String phongCach = tblDSSanPham.getValueAt(selectedRow, 6).toString();
//            String nhapSoLuong = tblDSSanPham.getValueAt(selectedRow, 7).toString();
//
//            // Đẩy dữ liệu lên các thành phần giao diện
//            txtMaSPCT.setText(maSPCT);
//            txtTenSanPham.setText(tenSanPham);
//            txtLoaiSPpp.setText(loaiCuaSP);
//            txtMauSaccc.setText(mauSac);
//            txtKichThuoccc.setText(kichThuoc);
//            txtPhongCachhh.setText(phongCach);
//            // txtNhapSoLuong.setText(nhapSoLuong);
//        }


 int selectedRow = tblDSSanPham.getSelectedRow();

    // Kiểm tra xem có dòng nào được chọn không
    if (selectedRow >= 0) {
        // Lấy dữ liệu từ bảng và đẩy lên các thành phần giao diện
        String stt = tblDSSanPham.getValueAt(selectedRow, 0).toString();
        String maSPCT = tblDSSanPham.getValueAt(selectedRow, 1).toString();
        String tenSanPham = tblDSSanPham.getValueAt(selectedRow, 2).toString();
        String loaiCuaSP = tblDSSanPham.getValueAt(selectedRow, 3).toString();
        String mauSac = tblDSSanPham.getValueAt(selectedRow, 4).toString();
        String kichThuoc = tblDSSanPham.getValueAt(selectedRow, 5).toString();
        String phongCach = tblDSSanPham.getValueAt(selectedRow, 6).toString();
        String nhapSoLuong = tblDSSanPham.getValueAt(selectedRow, 7).toString();

        // Đẩy dữ liệu lên các thành phần giao diện
        txtMaSPCT.setText(maSPCT);
        txtTenSanPham.setText(tenSanPham);
        txtLoaiSPpp.setText(loaiCuaSP);
        txtMauSaccc.setText(mauSac);
        txtKichThuoccc.setText(kichThuoc);
        txtPhongCachhh.setText(phongCach);
        // txtNhapSoLuong.setText(nhapSoLuong);

        // Thiết lập cho các trường văn bản chỉ đọc
        txtMaSPCT.setEditable(false);
        txtTenSanPham.setEditable(false);
        txtLoaiSPpp.setEditable(false);
        txtMauSaccc.setEditable(false);
        txtKichThuoccc.setEditable(false);
        txtPhongCachhh.setEditable(false);
        // txtNhapSoLuong.setEditable(false);
    }
    }//GEN-LAST:event_tblDSSanPhamMouseClicked
   
       
    private ArrayList<Object[]> danhSachSanPhamDaChon = new ArrayList<>();
    private void btnThemSPVaoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSPVaoHDActionPerformed
 try {
        // Lấy số lượng nhập vào từ ô nhập
        int soLuongNhapVao;
        try {
            soLuongNhapVao = Integer.parseInt(txtNhapSoLuong.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra số lượng nhập vào
        if (soLuongNhapVao <= 0) {
            JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy thông tin từ bảng Danh sách sản phẩm
        int selectedRow = tblDSSanPham.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn một sản phẩm từ danh sách", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int soLuongHienCo;
        double giaBan;
        try {
            soLuongHienCo = Integer.parseInt(tblDSSanPham.getValueAt(selectedRow, 7).toString()); // Cột số lượng ở vị trí thứ 7
            giaBan = Double.parseDouble(tblDSSanPham.getValueAt(selectedRow, 8).toString()); // Cột giá bán ở vị trí thứ 8
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu trong bảng không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (soLuongNhapVao > soLuongHienCo) {
            JOptionPane.showMessageDialog(this, "Số lượng nhập vào vượt quá số lượng hiện có", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy thông tin sản phẩm từ bảng tblDSSanPham
        String maSPCT = tblDSSanPham.getValueAt(selectedRow, 1).toString();
        String tenSP = tblDSSanPham.getValueAt(selectedRow, 2).toString();
        String loaiSP = tblDSSanPham.getValueAt(selectedRow, 3).toString();
        String mauSac = tblDSSanPham.getValueAt(selectedRow, 4).toString();
        String kichThuoc = tblDSSanPham.getValueAt(selectedRow, 5).toString();
        String phongCach = tblDSSanPham.getValueAt(selectedRow, 6).toString();

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        DefaultTableModel modelTrongHD = (DefaultTableModel) tblDSSPtrongHD.getModel();
        boolean found = false;
        int stt = modelTrongHD.getRowCount() + 1; // STT là số thứ tự của hàng mới

        for (int i = 0; i < modelTrongHD.getRowCount(); i++) {
            if (modelTrongHD.getValueAt(i, 1).toString().equals(maSPCT)) { // Kiểm tra mã SPCT
                int soLuongHienCoTrongHD = Integer.parseInt(modelTrongHD.getValueAt(i, 7).toString());
                int soLuongMoiTrongHD = soLuongHienCoTrongHD + soLuongNhapVao;
                modelTrongHD.setValueAt(soLuongMoiTrongHD, i, 7); // Cập nhật lại số lượng trong giỏ hàng
                found = true;
                break;
            }
        }

        if (!found) {
            // Thêm sản phẩm mới vào giỏ hàng nếu chưa có
            modelTrongHD.addRow(new Object[]{stt, maSPCT, tenSP, loaiSP, mauSac, kichThuoc, phongCach, soLuongNhapVao, giaBan});
        }

        // Cập nhật số lượng sản phẩm trong bảng tblDSSanPham
        int soLuongMoi = soLuongHienCo - soLuongNhapVao;
        tblDSSanPham.setValueAt(soLuongMoi, selectedRow, 7); // Cập nhật lại cột số lượng
        danhSachSanPhamDaChon.add(new Object[]{stt, maSPCT, tenSP, loaiSP, mauSac, kichThuoc, phongCach, soLuongNhapVao, giaBan});
        tinhTongTien();
        CapNhatGiaKhiChonVoucher();
        txtNhapSoLuong.setText("");
        lamMoi();

    } catch (NumberFormatException e) {
        // In chi tiết lỗi ra console để dễ dàng kiểm tra nguyên nhân
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
  
    }//GEN-LAST:event_btnThemSPVaoHDActionPerformed

       private void tinhTongTien() {
        double tongTienChuaVAT = 0;
        double VAT = 0.08;

        for (int i = 0; i < tblDSSPtrongHD.getRowCount(); i++) {
            int soLuong = Integer.parseInt(tblDSSPtrongHD.getValueAt(i, 7).toString());
        // Chuyển đổi giá bán từ chuỗi tiền tệ sang double
            String giaBanString = tblDSSPtrongHD.getValueAt(i, 8).toString().replace(" VNĐ", "").replace(",", ".");
            double giaBan = Double.parseDouble(giaBanString);
            tongTienChuaVAT += soLuong * giaBan;
        }

    double tienVAT = tongTienChuaVAT * VAT;
    double tongTienCoVAT = tongTienChuaVAT + tienVAT;

   
    lblTienTruocKhiGiam.setText(String.format("%.2f VNĐ", tongTienCoVAT));
   lblTongTienThanhToan.setText(String.format("%.2f VNĐ", tongTienCoVAT));
   
}
    


   
    private void btnCapNhatGioHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatGioHangActionPerformed
   
     int selectedRowTrongHD = tblDSSPtrongHD.getSelectedRow();
    if (selectedRowTrongHD != -1) {
        String giaTriNhapVao = txtNhapSoLuong1.getText().trim();
        System.out.println("Giá trị nhập vào: " + giaTriNhapVao);

        int soLuongMoi;
        try {
            soLuongMoi = Integer.parseInt(giaTriNhapVao);
            if (soLuongMoi <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi số lượng nhập vào: " + giaTriNhapVao);
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel modelTrongHD = (DefaultTableModel) tblDSSPtrongHD.getModel();
        int soLuongCu = Integer.parseInt(modelTrongHD.getValueAt(selectedRowTrongHD, 7).toString().trim()); // Số lượng cũ ở cột thứ 7

        int soLuongChenhLech = soLuongMoi - soLuongCu;

        String maSPCT = modelTrongHD.getValueAt(selectedRowTrongHD, 1).toString().trim(); // Mã SPCT ở cột thứ 1

        DefaultTableModel modelDanhSachSP = (DefaultTableModel) tblDSSanPham.getModel();
        for (int i = 0; i < modelDanhSachSP.getRowCount(); i++) {
            String maSPCTTrongDanhSach = modelDanhSachSP.getValueAt(i, 1).toString().trim(); // Mã SPCT ở cột thứ 1
            if (maSPCT.equals(maSPCTTrongDanhSach)) {
                try {
                    int soLuongHienCo = Integer.parseInt(modelDanhSachSP.getValueAt(i, 7).toString().trim()); // Số lượng hiện có ở cột thứ 7
                    int soLuongMoiTrongDanhSach = soLuongHienCo - soLuongChenhLech;
                    if (soLuongMoiTrongDanhSach < 0) {
                        JOptionPane.showMessageDialog(this, "Số lượng nhập vào vượt quá số lượng hiện có", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    modelDanhSachSP.setValueAt(soLuongMoiTrongDanhSach, i, 7);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Lỗi dữ liệu trong bảng danh sách sản phẩm: " + modelDanhSachSP.getValueAt(i, 7));
                    JOptionPane.showMessageDialog(this, "Dữ liệu trong bảng danh sách sản phẩm không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        modelTrongHD.setValueAt(soLuongMoi, selectedRowTrongHD, 7);

        tinhTongTien();
        CapNhatGiaKhiChonVoucher();

        txtNhapSoLuong1.setText("");
    } else {
        JOptionPane.showMessageDialog(this, "Chọn một sản phẩm từ giỏ hàng để cập nhật", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }



    }//GEN-LAST:event_btnCapNhatGioHangActionPerformed

    private void tblDSSPtrongHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSSPtrongHDMouseClicked
    int selectedRow = tblDSSPtrongHD.getSelectedRow();
    if (selectedRow != -1) {
        int soLuong = Integer.parseInt(tblDSSPtrongHD.getValueAt(selectedRow, 7).toString());
        txtNhapSoLuong1.setText(String.valueOf(soLuong));
    }

    }//GEN-LAST:event_tblDSSPtrongHDMouseClicked

    private void btnThemKHtuHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemKHtuHDActionPerformed
  String tenKH = txtTenKhachHang.getText().trim();
    String sdtKH = txtSDTKhachHang.getText().trim();

    if (!tenKH.isEmpty() && !sdtKH.isEmpty()) {
        try {
            ql.themKhachHang(tenKH, sdtKH); // Gọi phương thức themKhachHang
//            ql.loadKhachHang(tblKhachH);   
this.qlkh.loadTableCus(this.tblKhachH, this.qlkh.getAllCustomer());
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            // Hiển thị thông báo lỗi từ phương thức themKhachHang
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm khách hàng: " + e.getMessage(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_btnThemKHtuHDActionPerformed
    
    
    private void btnXoaKhoiGioHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaKhoiGioHangActionPerformed
             int selectedRow = tblDSSPtrongHD.getSelectedRow();
    if (selectedRow != -1) {
        DefaultTableModel modelTrongHD = (DefaultTableModel) tblDSSPtrongHD.getModel();

        // Lấy thông tin sản phẩm từ giỏ hàng
        String maSPCT = modelTrongHD.getValueAt(selectedRow, 1).toString();
        int soLuongXoa = Integer.parseInt(modelTrongHD.getValueAt(selectedRow, 7).toString());

        // Cập nhật số lượng sản phẩm trong bảng tblDSSanPham
        DefaultTableModel modelDanhSachSP = (DefaultTableModel) tblDSSanPham.getModel();
        for (int i = 0; i < modelDanhSachSP.getRowCount(); i++) {
            String maSPCTTrongDanhSach = modelDanhSachSP.getValueAt(i, 1).toString(); // Mã SPCT ở cột 1
            if (maSPCT.equals(maSPCTTrongDanhSach)) {
                int soLuongHienCo = Integer.parseInt(modelDanhSachSP.getValueAt(i, 7).toString()); // Số lượng ở cột 7
                int soLuongMoi = soLuongHienCo + soLuongXoa; // Tính số lượng mới
                modelDanhSachSP.setValueAt(soLuongMoi, i, 7); // Cập nhật số lượng mới vào bảng danh sách sản phẩm
                break;
            }
        }

        // Xóa sản phẩm khỏi giỏ hàng
        modelTrongHD.removeRow(selectedRow);

        // Cập nhật tổng tiền
        tinhTongTien();
        CapNhatGiaKhiChonVoucher();

       
        txtLoaiSPpp.setText("");
        txtMauSaccc.setText("");
        txtKichThuoccc.setText("");
        txtPhongCachhh.setText("");
        txtNhapSoLuong1.setText("");
    } else {
        JOptionPane.showMessageDialog(this, "Chọn một sản phẩm từ giỏ hàng để xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

  
    }//GEN-LAST:event_btnXoaKhoiGioHangActionPerformed
   
    private void jpQLHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpQLHDMouseClicked
      this.jpViewQLHD.setVisible(true);
        this.jpViewQLDM.setVisible(false);
        this.jpViewQLKH.setVisible(false);
        this.jpViewQLKM.setVisible(false);
        this.jpViewQLSPP.setVisible(false);
        this.jpViewQLTK.setVisible(false);
        this.jpViewTK.setVisible(false);
        this.jpViewQLBH2.setVisible(false);
        
        this.jpViewDoiMK.setVisible(false);
        this.jpViewQLSPP2.setVisible(false);
        this.jpViewQLSPP3.setVisible(false);
        
        
        this.jpQLBH.setBackground(new Color(255,102,0));
        this.jpQLHD.setBackground(new Color(220, 220, 220));
        this.jpQLDM.setBackground(new Color(255,102,0));
        this.jpQLKH.setBackground(new Color(255,102,0));
        this.jpQLKM.setBackground(new Color(255,102,0));
        this.jpQLSP.setBackground(new Color(255,102,0));
        this.jpQLTK.setBackground(new Color(255,102,0));
        this.jpTK.setBackground(new Color(255,102,0));
        
    }//GEN-LAST:event_jpQLHDMouseClicked

    private void txtSDTKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSDTKhachHangActionPerformed
    String soDienThoai = txtSDTKhachHang.getText().trim();
      
    boolean found = false;
    for (int i = 0; i < tblKhachH.getRowCount(); i++) {
        String sdtTrongBang = tblKhachH.getValueAt(i, 3).toString();
        if (sdtTrongBang.equals(soDienThoai)) {
            String tenKhachHang = tblKhachH.getValueAt(i, 2).toString(); // Cột tên khách hàng
            int diemTichLuy = Integer.parseInt(tblKhachH.getValueAt(i, 4).toString()); // Cột điểm tích lũy
            int diemDaDung = Integer.parseInt(tblKhachH.getValueAt(i, 5).toString()); // Cột điểm đã dùng
            int diemConLai = diemTichLuy - diemDaDung; // Tính điểm còn lại

            txtTenKhachHang.setText(tenKhachHang);
            lblDiemConLai.setText(String.valueOf(diemConLai));

            found = true;
            break;
        }
    }
    if (!found) {
        JOptionPane.showMessageDialog(null, "Số điện thoại này chưa có tài khoản", "Thông báo", JOptionPane.ERROR_MESSAGE);
        
        lblDiemConLai.setText("0");
    }
    tblHoaDonCho.clearSelection();
    }//GEN-LAST:event_txtSDTKhachHangActionPerformed

    private void btnLuuTruHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuTruHDActionPerformed

    try {
    String tenKhachHang = txtTenKhachHang.getText().trim();
    String soDienThoai = txtSDTKhachHang.getText().trim();
    int diemDaDung = Integer.parseInt(txtSoDiemDaDung.getText().trim().isEmpty() ? "0" : txtSoDiemDaDung.getText().trim());
    String phuongThucThanhToan = rdoTienMat.isSelected() ? "Tiền mặt" : "Chuyển khoản";
    String voucherCode = (String) cbbVouchers.getSelectedItem(); // Lấy mã voucher từ combobox

    // Lấy thông tin giảm giá từ voucher
    double voucherDiscountPercent = 0;
    double maximumReduce = 0;
    double moneyApplies = 0;

    if (voucherCode != null && !voucherCode.isEmpty()) {
        // Lấy phần trăm giảm giá từ voucher
        voucherDiscountPercent = getVoucherDiscountPercent(voucherCode);

        // Lấy các thông tin liên quan đến voucher
        String query = "SELECT money_applies, maximum_reduce, quantity_voucher FROM Voucher WHERE voucher_code = ?";
        try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, voucherCode);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                moneyApplies = rs.getDouble("money_applies");
                maximumReduce = rs.getDouble("maximum_reduce");
            }
        }
    }

    // Tính lại tổng tiền thanh toán sau khi áp dụng voucher
    double tongTienChuaVAT = 0;
    double VAT = 0.08; // Giá trị VAT hiện tại là 8%

    for (int i = 0; i < tblDSSPtrongHD.getRowCount(); i++) {
        int soLuong = Integer.parseInt(tblDSSPtrongHD.getValueAt(i, 7).toString());
        String giaBanString = tblDSSPtrongHD.getValueAt(i, 8).toString().replace(" VNĐ", "").replace(",", ".");
        double giaBan = Double.parseDouble(giaBanString);
        tongTienChuaVAT += soLuong * giaBan;
    }

    double tienVAT = tongTienChuaVAT * VAT;
    double tongTienCoVAT = tongTienChuaVAT + tienVAT;
    double tongTienSauVoucher = tongTienCoVAT; // Khởi tạo tổng tiền sau khi giảm

    // Kiểm tra điều kiện giảm giá
    if (tongTienCoVAT >= moneyApplies) {
        double discountAmount = tongTienCoVAT * (voucherDiscountPercent / 100);
        // Giảm tối đa theo cột maximum_reduce
        if (discountAmount > maximumReduce) {
            discountAmount = maximumReduce;
        }
        tongTienSauVoucher -= discountAmount; // Áp dụng giảm giá
    } else {
        // Không đủ điều kiện để áp dụng voucher
        voucherCode = null; // Đặt voucherCode thành null nếu không đủ điều kiện
    }
    
    
    lblTienTruocKhiGiam.setText(String.format("%.2f VNĐ", tongTienCoVAT));
    lblTongTienThanhToan.setText(String.format("%.2f VNĐ", tongTienSauVoucher));

    if (tenKhachHang.isEmpty() || soDienThoai.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin khách hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (tblDSSPtrongHD.getRowCount() == 0) {
        JOptionPane.showMessageDialog(null, "Giỏ hàng chưa có sản phẩm.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    
    if (tblHoaDonCho.getSelectedRowCount() == 0) {
        // Kiểm tra xem số điện thoại có tồn tại trong tblHoaDonCho không
        boolean soDienThoaiTonTai = false;
        for (int i = 0; i < tblHoaDonCho.getRowCount(); i++) {
            String soDienThoaiTrongHDCho = tblHoaDonCho.getValueAt(i, 4).toString(); // Cột thứ 5 là số điện thoại
            if (soDienThoaiTrongHDCho.equals(soDienThoai)) {
                soDienThoaiTonTai = true;
                break;
            }
        }

        if (soDienThoaiTonTai) {
            JOptionPane.showMessageDialog(null, "Số điện thoại này đã tồn tại trong hóa đơn chờ khác rồi.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return; // Dừng lại không cho tiếp tục xử lý
        }
    }

    // Lấy tên nhân viên
    String tenNhanVien = getEmployeeNameById(this.currentUserId);
    lblNhanVienBan.setText(tenNhanVien != null ? tenNhanVien : "Không tìm thấy tên nhân viên");

    // Kiểm tra xem có hóa đơn đã lưu trữ nào được chọn hay không
    long selectedOrderId = -1;
    String maHoaDon = ""; // Thay đổi này để giữ mã hóa đơn
    try {
        selectedOrderId = getSelectedOrderId(); // Lấy ID hóa đơn đã chọn từ giao diện
        maHoaDon = tblHoaDonCho.getValueAt(tblHoaDonCho.getSelectedRow(), 1).toString(); // Giả sử cột 1 là mã hóa đơn
    } catch (SQLException e) {
        // Nếu không có hóa đơn nào được chọn, cho phép tạo hóa đơn mới
        selectedOrderId = -1; // Không cần làm gì thêm, sẽ tạo mới hóa đơn
    }

    String queryHoaDon;

    if (selectedOrderId != -1) { // Nếu có hóa đơn đã chọn
        queryHoaDon = "UPDATE Orders SET customer_id = ?, voucher_id = (SELECT voucher_id FROM Voucher WHERE voucher_code = ?), VAT = ?, payment_method = ?, total_price = ?, last_update_date_point = ? WHERE order_id = ?";
    } else { // Nếu không có hóa đơn đã chọn, tạo mới
        queryHoaDon = "INSERT INTO Orders (user_id, order_date, status, customer_id, voucher_id, VAT, payment_method, total_price, last_update_date_point) VALUES (?, ?, ?, ?, (SELECT voucher_id FROM Voucher WHERE voucher_code = ?), ?, ?, ?, ?)";
    }

    try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
         PreparedStatement pstHoaDon = connection.prepareStatement(queryHoaDon, Statement.RETURN_GENERATED_KEYS)) {

        int customerId = getCustomerId(tenKhachHang, soDienThoai);

        if (selectedOrderId != -1) {
            capNhatSoLuongKho(connection, maHoaDon);
            // Cập nhật hóa đơn đã chọn
            pstHoaDon.setInt(1, customerId);
            pstHoaDon.setString(2, voucherCode); // Sử dụng voucherCode đã được cập nhật
            pstHoaDon.setDouble(3, VAT * 100);
            pstHoaDon.setString(4, phuongThucThanhToan);
            pstHoaDon.setDouble(5, tongTienSauVoucher);
            pstHoaDon.setDate(6, new java.sql.Date(System.currentTimeMillis()));
            pstHoaDon.setLong(7, selectedOrderId);
        } else {
            // Tạo hóa đơn mới
            pstHoaDon.setInt(1, this.currentUserId);
            pstHoaDon.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            pstHoaDon.setString(3, "Chưa thanh toán"); // Đặt trạng thái là "Chưa thanh toán"
            pstHoaDon.setInt(4, customerId);
            pstHoaDon.setString(5, voucherCode); // Sử dụng voucherCode đã được cập nhật
            pstHoaDon.setDouble(6, VAT * 100);
            pstHoaDon.setString(7, phuongThucThanhToan);
            pstHoaDon.setDouble(8, tongTienSauVoucher); // Cập nhật tổng tiền thanh toán đã áp dụng voucher
            pstHoaDon.setDate(9, new java.sql.Date(System.currentTimeMillis()));
        }

        int affectedRows = pstHoaDon.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Lưu trữ hóa đơn thất bại, không có hàng nào bị ảnh hưởng.");
        }

        if (selectedOrderId != -1) {
            // Nếu có hóa đơn đã chọn, cập nhật chi tiết hóa đơn
            CapNhatHDCT(connection, maHoaDon);
        } else {
            try (ResultSet generatedKeys = pstHoaDon.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long maHoaDonMoi = generatedKeys.getLong(1);
                    insertOrderDetails(maHoaDonMoi); // Thêm chi tiết đơn hàng
                } else {
                    throw new SQLException("Lưu trữ hóa đơn thất bại, không có ID được lấy.");
                }
            }
        }

        JOptionPane.showMessageDialog(null, "Lưu trữ hóa đơn thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        resetInvoiceData();

        tblDSHD.setModel(qlHoaDon.loadOrderData());
        ql.LoadDSSPTrongQLBH(tblDSSanPham);
        ql.DanhSachHoaDonCho(tblHoaDonCho);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lưu trữ hóa đơn thất bại: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    isContinuingPayment = false;
} catch (NumberFormatException e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(null, "Lỗi định dạng số: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
} catch (SQLException ex) {
    Logger.getLogger(View_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
}

 
    }//GEN-LAST:event_btnLuuTruHDActionPerformed



private long getSelectedOrderId() throws SQLException {
    int selectedRow = tblHoaDonCho.getSelectedRow(); // Giả sử tblHoaDonCho là bảng chứa danh sách hóa đơn
    if (selectedRow == -1) {
        throw new SQLException("Chưa chọn hóa đơn nào.");
    }

    // Lấy mã hóa đơn từ cột tương ứng trong bảng (giả sử cột 1 là mã hóa đơn)
    String orderCode = tblHoaDonCho.getValueAt(selectedRow, 1).toString();

    // Kết nối với cơ sở dữ liệu để lấy order_id
    String query = "SELECT order_id FROM Orders WHERE order_code = ?";
    try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
         PreparedStatement pst = connection.prepareStatement(query)) {
        pst.setString(1, orderCode);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return rs.getLong("order_id");
        } else {
            throw new SQLException("Không tìm thấy hóa đơn.");
        }
    }
}


    private void tblHoaDonChoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonChoMouseClicked

    }//GEN-LAST:event_tblHoaDonChoMouseClicked
 
    private void btnXoaHoaDonChoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaHoaDonChoActionPerformed


        int selectedRow = tblHoaDonCho.getSelectedRow();
    if (selectedRow >= 0) {
        String orderCode = tblHoaDonCho.getValueAt(selectedRow, 1).toString(); // Lấy mã hóa đơn dạng chuỗi (HD002)
        int hoaDonId = 0;

        // Truy vấn để lấy order_id dựa trên order_code
        String queryGetOrderId = "SELECT order_id FROM Orders WHERE RIGHT('HD' + RIGHT('000' + CAST(order_id AS VARCHAR(3)), 3), LEN(?)) = ?";
        String queryDeleteOrderDetails = "DELETE FROM OrderDetails WHERE order_id = ?";
        String queryDeleteOrder = "DELETE FROM Orders WHERE order_id = ?";
        String queryUpdateProductDetails = "UPDATE ProductDetails SET quantity = quantity + ? WHERE productDetails_id = ?";
        String querySelectOrderDetails = "SELECT productDetails_id, quantity FROM OrderDetails WHERE order_id = ?";

        Connection connection = null;
        try {
            // Khởi tạo kết nối
            connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
            // Bắt đầu giao dịch
            connection.setAutoCommit(false);

            // Lấy order_id từ order_code
            try (PreparedStatement pstGetOrderId = connection.prepareStatement(queryGetOrderId)) {
                pstGetOrderId.setString(1, orderCode);
                pstGetOrderId.setString(2, orderCode);
                try (ResultSet rs = pstGetOrderId.executeQuery()) {
                    if (rs.next()) {
                        hoaDonId = rs.getInt("order_id");
                    } else {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy hóa đơn với mã: " + orderCode, "Thông báo", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            // Lấy thông tin chi tiết đơn hàng để cập nhật lại số lượng sản phẩm
            try (PreparedStatement pstSelectOrderDetails = connection.prepareStatement(querySelectOrderDetails)) {
                pstSelectOrderDetails.setInt(1, hoaDonId);
                try (ResultSet rs = pstSelectOrderDetails.executeQuery()) {
                    while (rs.next()) {
                        int productDetailsId = rs.getInt("productDetails_id");
                        int quantity = rs.getInt("quantity");

                        // Cập nhật lại số lượng sản phẩm
                        try (PreparedStatement pstUpdateProductDetails = connection.prepareStatement(queryUpdateProductDetails)) {
                            pstUpdateProductDetails.setInt(1, quantity);
                            pstUpdateProductDetails.setInt(2, productDetailsId);
                            pstUpdateProductDetails.executeUpdate();
                        }
                    }
                }
            }

            // Xóa chi tiết hóa đơn khỏi bảng OrderDetails
            try (PreparedStatement pstDeleteOrderDetails = connection.prepareStatement(queryDeleteOrderDetails)) {
                pstDeleteOrderDetails.setInt(1, hoaDonId);
                pstDeleteOrderDetails.executeUpdate();
            }

            // Xóa hóa đơn khỏi bảng Orders
            try (PreparedStatement pstDeleteOrder = connection.prepareStatement(queryDeleteOrder)) {
                pstDeleteOrder.setInt(1, hoaDonId);
                pstDeleteOrder.executeUpdate();
            }

            // Commit giao dịch
            connection.commit();

            // Nếu đang trong quá trình tiếp tục thanh toán, xóa sản phẩm trong giỏ hàng
            if (isContinuingPayment) {
                DefaultTableModel model = (DefaultTableModel) tblDSSPtrongHD.getModel();
                model.setRowCount(0); // Xóa toàn bộ sản phẩm trong giỏ hàng
                lblTongTienThanhToan.setText("0 VNĐ"); // Đặt lại tổng tiền thanh toán
                isContinuingPayment = false; // Đặt lại trạng thái
            }

            // Cập nhật dữ liệu bảng và thông báo thành công
            JOptionPane.showMessageDialog(null, "Xóa hóa đơn thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            ql.DanhSachHoaDonCho(tblHoaDonCho); // Cập nhật bảng hóa đơn chờ
            tblDSHD.setModel(qlHoaDon.loadOrderData());
            ql.LoadDSSPTrongQLBH(tblDSSanPham);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            // Rollback giao dịch nếu có lỗi
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Lỗi khi rollback giao dịch: " + rollbackEx.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } finally {
            // Đảm bảo đóng kết nối
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Lỗi khi đóng kết nối: " + closeEx.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    } else {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn hóa đơn để xóa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    }//GEN-LAST:event_btnXoaHoaDonChoActionPerformed

    private void txtMaCTSPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaCTSPKeyReleased
        String input = txtMaCTSP.getText().trim();
    DefaultTableModel model = (DefaultTableModel) tblDSSanPham.getModel();
    model.setRowCount(0);

    try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection()) {
        String query = "SELECT d.productDetails_code AS 'Mã SPCT', p.product_name AS 'Tên SP', c.category_name AS 'Loại SP', " +
                       "cl.color_name AS 'Màu sắc', s.size_name AS 'Kích thước', st.style_name AS 'Phong cách', " +
                       "d.quantity AS 'Số lượng', d.price AS 'Giá bán' " +
                       "FROM Products p " +
                       "JOIN Categories c ON p.category_id = c.category_id " +
                       "JOIN ProductDetails d ON p.product_id = d.product_id " +
                       "JOIN Color cl ON d.color_id = cl.color_id " +
                       "JOIN Styles st ON d.style_id = st.style_id " +
                       "JOIN Sizes s ON d.size_id = s.size_id " +
                       "WHERE d.productDetails_code LIKE ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, input + "%");

            ResultSet rs = pst.executeQuery();
            int stt = 1;
            while (rs.next()) {
                
                String maSPCT = rs.getString("Mã SPCT");
                String tenSP = rs.getString("Tên SP");
                String loaiSP = rs.getString("Loại SP");
                String mauSac = rs.getString("Màu sắc");
                String kichThuoc = rs.getString("Kích thước");
                String phongCach = rs.getString("Phong cách");
                int soLuong = rs.getInt("Số lượng");
                double giaBan = rs.getDouble("Giá bán");

                // Thêm dòng dữ liệu vào bảng
                model.addRow(new Object[]{stt,maSPCT, tenSP, loaiSP, mauSac, kichThuoc, phongCach, soLuong, giaBan});
                 stt++;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_txtMaCTSPKeyReleased
    private void lamMoi(){
    txtMaSPCT.setText("");
    txtTenSanPham.setText("");
    txtNhapSoLuong.setText(""); 
    txtMauSaccc.setText("");
    txtPhongCachhh.setText("");
    txtKichThuoccc.setText("");
    txtLoaiSPpp.setText(""); 
    }
    private void btnLamMoiTTSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiTTSPActionPerformed
         this.lamMoi();  
    }//GEN-LAST:event_btnLamMoiTTSPActionPerformed
  
    private boolean isContinuingPayment = false;
    private int dongDaChonTruocDo = -1;    

    private void btnTiepTucThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTiepTucThanhToanActionPerformed
    int selectedRow = tblHoaDonCho.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn một hóa đơn để tiếp tục thanh toán.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
     ql.LoadDSSPTrongQLBH(tblDSSanPham);

    String maHoaDon = tblHoaDonCho.getValueAt(selectedRow, 1).toString();

    // Kiểm tra nếu đã chọn hóa đơn chờ khác
    if (isContinuingPayment && maHoaDon.equals(tblHoaDonCho.getValueAt(dongDaChonTruocDo, 1).toString())) {
        JOptionPane.showMessageDialog(null, "Hóa đơn này đang trong quá trình thanh toán. Vui lòng thanh toán cho xong.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Cập nhật hàng hóa đơn đã chọn trước đó
    dongDaChonTruocDo = selectedRow;

    try {
         //Kiểm tra giỏ hàng có sản phẩm hay không
        if (tblDSSPtrongHD.getRowCount() > 0) {
            int confirm = JOptionPane.showConfirmDialog(null, 
                    "Trong giỏ hàng đang có sản phẩm. Bạn có muốn lưu trữ hóa đơn không?", 
                    "Xác nhận", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                themHDCvaCapNhatHDC();
                    tblHoaDonCho.setRowSelectionInterval(selectedRow, selectedRow); 
            }
            else{
//                xoaKhoiGioHang();
                ql.LoadDSSPTrongQLBH(tblDSSanPham);
            }
        }


        // Lấy thông tin hóa đơn từ cơ sở dữ liệu
        String queryHoaDon = "SELECT * FROM Orders WHERE order_code = ?";
        try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
             PreparedStatement pstHoaDon = connection.prepareStatement(queryHoaDon)) {
            pstHoaDon.setString(1, maHoaDon);
            try (ResultSet rsHoaDon = pstHoaDon.executeQuery()) {
                if (rsHoaDon.next()) {
                    // Lấy thông tin khách hàng
                    int customerId = rsHoaDon.getInt("customer_id");
                    String queryCustomer = "SELECT * FROM Customers WHERE customer_id = ?";
                    try (PreparedStatement pstCustomer = connection.prepareStatement(queryCustomer)) {
                        pstCustomer.setInt(1, customerId);
                        try (ResultSet rsCustomer = pstCustomer.executeQuery()) {
                            if (rsCustomer.next()) {
                                txtTenKhachHang.setText(rsCustomer.getString("customer_name"));
                                txtSDTKhachHang.setText(rsCustomer.getString("phone"));
                                int pointGained = rsCustomer.getInt("point_gained");
                                int pointsUsed = rsCustomer.getInt("points_used");
                                lblDiemConLai.setText(String.valueOf(pointGained - pointsUsed)); // Tính điểm còn lại
                            }
                        }
                    }

                    // Lấy thông tin sản phẩm trong hóa đơn
                    String queryOrderDetails = "SELECT od.*, pd.productDetails_code, p.product_name, c.category_name, co.color_name, s.size_name, st.style_name " +
                                                "FROM OrderDetails od " +
                                                "JOIN ProductDetails pd ON od.productDetails_id = pd.productDetails_id " +
                                                "JOIN Products p ON pd.product_id = p.product_id " +
                                                "JOIN Categories c ON p.category_id = c.category_id " +
                                                "JOIN Color co ON pd.color_id = co.color_id " +
                                                "JOIN Sizes s ON pd.size_id = s.size_id " +
                                                "JOIN Styles st ON pd.style_id = st.style_id " +
                                                "WHERE od.order_id = ?";
                    try (PreparedStatement pstOrderDetails = connection.prepareStatement(queryOrderDetails)) {
                        pstOrderDetails.setInt(1, rsHoaDon.getInt("order_id"));
                        try (ResultSet rsOrderDetails = pstOrderDetails.executeQuery()) {
                            DefaultTableModel model = (DefaultTableModel) tblDSSPtrongHD.getModel();
                            model.setRowCount(0); // Xóa dữ liệu cũ
                            double tongTienChuaVAT = 0;
                            double VAT = 0.08; // 8%
                            int stt = 1; // Biến đếm số thứ tự
                            while (rsOrderDetails.next()) {
                                // Thêm từng sản phẩm vào bảng
                                int soLuong = rsOrderDetails.getInt("quantity");
                                double giaBan = rsOrderDetails.getDouble("unit_price");
                                tongTienChuaVAT += soLuong * giaBan;

                                Object[] row = new Object[]{
                                    stt++, // STT
                                    rsOrderDetails.getString("productDetails_code"), // Hiển thị productDetails_code
                                    rsOrderDetails.getString("product_name"),
                                    rsOrderDetails.getString("category_name"),
                                    rsOrderDetails.getString("color_name"),
                                    rsOrderDetails.getString("size_name"),
                                    rsOrderDetails.getString("style_name"),
                                    soLuong,
                                    String.format("%.2f VNĐ", giaBan)
                                };
                                model.addRow(row);
                            }

                            // Tính VAT và tổng tiền
                            double tienVAT = tongTienChuaVAT * VAT;
                            double tongTienCoVAT = tongTienChuaVAT + tienVAT;

                            // Lấy thông tin voucher đã áp dụng
                            Integer voucherId = rsHoaDon.getInt("voucher_id");
                            double tongTienSauVoucher = tongTienCoVAT; // Khởi tạo với tổng tiền có VAT
                            if (voucherId != null) {
                                String queryVoucher = "SELECT voucher_code FROM Voucher WHERE voucher_id = ?";
                                try (PreparedStatement pstVoucher = connection.prepareStatement(queryVoucher)) {
                                    pstVoucher.setInt(1, voucherId);
                                    try (ResultSet rsVoucher = pstVoucher.executeQuery()) {
                                        if (rsVoucher.next()) {
                                            String voucherCode = rsVoucher.getString("voucher_code");
                                            cbbVouchers.setSelectedItem(voucherCode);
                                            double maxDiscount = 40.0; // Giả sử giảm tối đa là 40 VNĐ
                                            double discountPercent = getVoucherDiscountPercent(voucherCode);

                                            // Tính số tiền giảm giá tối đa
                                            double calculatedDiscount = tongTienCoVAT * (discountPercent / 100);
                                            double finalDiscount = Math.min(calculatedDiscount, maxDiscount);

                                            // Tính tổng tiền sau khi áp dụng voucher
                                            tongTienSauVoucher -= finalDiscount;
                                        }
                                    }
                                }
                            } else {
                                cbbVouchers.setSelectedIndex(-1);
                            }

                            double tongTienDaLuu = rsHoaDon.getDouble("total_price");
                            lblTienTruocKhiGiam.setText(String.format("%.2f VNĐ", tongTienCoVAT));
                            lblTongTienThanhToan.setText(String.format("%.2f VNĐ", tongTienDaLuu));
                        }
                    }

                    // Lấy phương thức thanh toán
                    String paymentMethod = rsHoaDon.getString("payment_method");
                    if (paymentMethod.equals("Tiền mặt")) {
                        rdoTienMat.setSelected(true);
                    } else {
                        rdoChuyenKhoan.setSelected(true);
                    }
                }
            }
        }
        isContinuingPayment = true; // Thiết lập biến khi bấm "Tiếp tục thanh toán"
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi lấy thông tin hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    }//GEN-LAST:event_btnTiepTucThanhToanActionPerformed
       

    
    private void capNhatSoLuong(String maHoaDon) throws SQLException {
    // Lấy số lượng sản phẩm cũ từ hóa đơn
    String getOrderDetailsQuery = "SELECT productDetails_id, quantity FROM OrderDetails WHERE order_id = (SELECT order_id FROM Orders WHERE order_code = ?)";
    Map<Integer, Integer> oldQuantities = new HashMap<>();

    try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
            PreparedStatement pstGetOrderDetails = connection.prepareStatement(getOrderDetailsQuery)) {
        pstGetOrderDetails.setString(1, maHoaDon);
        ResultSet rs = pstGetOrderDetails.executeQuery();
        while (rs.next()) {
            int productDetailsId = rs.getInt("productDetails_id");
            int quantity = rs.getInt("quantity");
            oldQuantities.put(productDetailsId, quantity);
        }
    }

    // Tạo ánh xạ từ productDetails_code đến productDetails_id
    String getProductIdQuery = "SELECT productDetails_id FROM ProductDetails WHERE productDetails_code = ?";
    Map<String, Integer> productCodeToId = new HashMap<>();
    
    for (int i = 0; i < tblDSSPtrongHD.getRowCount(); i++) {
        String productDetailsCode = tblDSSPtrongHD.getValueAt(i, 1).toString();
        if (!productCodeToId.containsKey(productDetailsCode)) {
            try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
                    PreparedStatement pstGetProductId = connection.prepareStatement(getProductIdQuery)) {
                pstGetProductId.setString(1, productDetailsCode);
                ResultSet rs = pstGetProductId.executeQuery();
                if (rs.next()) {
                    int productDetailsId = rs.getInt("productDetails_id");
                    productCodeToId.put(productDetailsCode, productDetailsId);
                }
            }
        }
    }

    // Cập nhật số lượng kho cho sản phẩm trong hóa đơn chờ
    String updateInventoryQuery = "UPDATE ProductDetails SET quantity = quantity + ? WHERE productDetails_id = ?";
    try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
            PreparedStatement pstUpdateInventory = connection.prepareStatement(updateInventoryQuery)) {
        for (Map.Entry<Integer, Integer> entry : oldQuantities.entrySet()) {
            int productDetailsId = entry.getKey();
            int oldQuantity = entry.getValue();
            
            // Kiểm tra nếu sản phẩm này vẫn còn trong giỏ hàng
            String productDetailsCode = null;
            for (int i = 0; i < tblDSSPtrongHD.getRowCount(); i++) {
                String code = tblDSSPtrongHD.getValueAt(i, 1).toString();
                if (productCodeToId.containsKey(code) && productCodeToId.get(code) == productDetailsId) {
                    productDetailsCode = code;
                    break;
                }
            }

            // Nếu không tìm thấy trong giỏ hàng, cộng lại số lượng vào kho
            if (productDetailsCode == null) {
                pstUpdateInventory.setInt(1, oldQuantity); // Cộng lại số lượng cũ vào kho
                pstUpdateInventory.setInt(2, productDetailsId);
                pstUpdateInventory.executeUpdate();
            }
        }
        
        // Cập nhật số lượng kho cho sản phẩm mới thêm vào giỏ
        for (int i = 0; i < tblDSSPtrongHD.getRowCount(); i++) {
            String productDetailsCode = tblDSSPtrongHD.getValueAt(i, 1).toString();
            String newQuantityStr = tblDSSPtrongHD.getValueAt(i, 7).toString();
            
            try {
                int newQuantity = Integer.parseInt(newQuantityStr);
                int productDetailsId = productCodeToId.getOrDefault(productDetailsCode, -1);
                
                if (productDetailsId != -1) {
                    int oldQuantity = oldQuantities.getOrDefault(productDetailsId, 0);
                    int quantityDifference = oldQuantity - newQuantity;
                    
                    if (quantityDifference != 0) {
                        pstUpdateInventory.setInt(1, quantityDifference); // Gia tăng nếu quantityDifference > 0, giảm nếu < 0
                        pstUpdateInventory.setInt(2, productDetailsId);
                        pstUpdateInventory.executeUpdate();
                    }
                }
            } catch (NumberFormatException e) {
                // Xử lý lỗi nếu giá trị không phải là số
                JOptionPane.showMessageDialog(null, "Có lỗi trong dữ liệu sản phẩm: " + e.getMessage(), "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

    
    private void CapNhatHDCTcuaHDC(String maHoaDon) throws SQLException {
    // Cập nhật chi tiết hóa đơn
    String updateOrderDetailsQuery = "DELETE FROM OrderDetails WHERE order_id = (SELECT order_id FROM Orders WHERE order_code = ?)";
    try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
            PreparedStatement pstUpdateOrderDetails = connection.prepareStatement(updateOrderDetailsQuery)) {
        pstUpdateOrderDetails.setString(1, maHoaDon);
        pstUpdateOrderDetails.executeUpdate();

        inSertHoaDonChiTiet(getOrderIdFromOrderCode(connection, maHoaDon));
    }

}
    
    private void themHDCvaCapNhatHDC() {
    try {
        String tenKhachHang = txtTenKhachHang.getText().trim();
        String soDienThoai = txtSDTKhachHang.getText().trim();
        int diemDaDung = Integer.parseInt(txtSoDiemDaDung.getText().trim().isEmpty() ? "0" : txtSoDiemDaDung.getText().trim());
        String phuongThucThanhToan = rdoTienMat.isSelected() ? "Tiền mặt" : "Chuyển khoản";
        String voucherCode = (String) cbbVouchers.getSelectedItem();

        boolean hoaDonChoTonTai = false;
        String maHoaDonCho = null;

        // Kiểm tra số điện thoại trùng trong tblHoaDonCho
        for (int i = 0; i < tblHoaDonCho.getRowCount(); i++) {
            String sdtKhachHangTrongHoaDonCho = tblHoaDonCho.getValueAt(i, 4).toString();
            if (soDienThoai.equals(sdtKhachHangTrongHoaDonCho)) {
                hoaDonChoTonTai = true;
                maHoaDonCho = tblHoaDonCho.getValueAt(i, 1).toString(); // Lấy mã hóa đơn chờ
                break;
            }
        }

        // Lấy thông tin giảm giá từ voucher
        double voucherDiscountPercent = 0;
        double maximumReduce = 0;
        double moneyApplies = 0;

        if (voucherCode != null && !voucherCode.isEmpty()) {
            voucherDiscountPercent = getVoucherDiscountPercent(voucherCode);

            String query = "SELECT money_applies, maximum_reduce FROM Voucher WHERE voucher_code = ?";
            try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
                 PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setString(1, voucherCode);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    moneyApplies = rs.getDouble("money_applies");
                    maximumReduce = rs.getDouble("maximum_reduce");
                }
            }
        }

        // Tính lại tổng tiền thanh toán sau khi áp dụng voucher
        double tongTienChuaVAT = 0;
        double VAT = 0.08;

        for (int i = 0; i < tblDSSPtrongHD.getRowCount(); i++) {
            int soLuong = Integer.parseInt(tblDSSPtrongHD.getValueAt(i, 7).toString());
            String giaBanString = tblDSSPtrongHD.getValueAt(i, 8).toString().replace(" VNĐ", "").replace(",", ".");
            double giaBan = Double.parseDouble(giaBanString);
            tongTienChuaVAT += soLuong * giaBan;
        }

        double tienVAT = tongTienChuaVAT * VAT;
        double tongTienCoVAT = tongTienChuaVAT + tienVAT;
        double tongTienSauVoucher = tongTienCoVAT;
       
            // Kiểm tra điều kiện giảm giá
    if (tongTienCoVAT >= moneyApplies) {
        double discountAmount = tongTienCoVAT * (voucherDiscountPercent / 100);
        // Giảm tối đa theo cột maximum_reduce
        if (discountAmount > maximumReduce) {
            discountAmount = maximumReduce;
        }
        tongTienSauVoucher -= discountAmount; // Áp dụng giảm giá
    } else {
        // Không đủ điều kiện để áp dụng voucher
        voucherCode = null; // Đặt voucherCode thành null nếu không đủ điều kiện
    }
        

        lblTongTienThanhToan.setText(String.format("%.2f VNĐ", tongTienSauVoucher));

        if (tenKhachHang.isEmpty() || soDienThoai.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin khách hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tblDSSPtrongHD.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Giỏ hàng chưa có sản phẩm.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String tenNhanVien = getEmployeeNameById(this.currentUserId);
        lblNhanVienBan.setText(tenNhanVien != null ? tenNhanVien : "Không tìm thấy tên nhân viên");

        if (hoaDonChoTonTai) {
            // Cập nhật hóa đơn chờ
            capNhatSoLuong(maHoaDonCho);
            try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection()) {
                String queryUpdateHoaDonCho = "UPDATE Orders SET total_price = ?, last_update_date_point = ? WHERE order_code = ?";
                try (PreparedStatement pstUpdate = connection.prepareStatement(queryUpdateHoaDonCho)) {
                    pstUpdate.setDouble(1, tongTienSauVoucher);
                    pstUpdate.setDate(2, new java.sql.Date(System.currentTimeMillis()));
                    pstUpdate.setString(3, maHoaDonCho);
                    pstUpdate.executeUpdate();
                }
                
                CapNhatHDCTcuaHDC(maHoaDonCho);
                

                JOptionPane.showMessageDialog(null, "Cập nhật hóa đơn chờ thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Cập nhật hóa đơn chờ thất bại: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Tạo hóa đơn mới
            try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection()) {
                String queryHoaDon = "INSERT INTO Orders (user_id, order_date, status, customer_id, voucher_id, VAT, payment_method, total_price, last_update_date_point) VALUES (?, ?, ?, ?, (SELECT voucher_id FROM Voucher WHERE voucher_code = ?), ?, ?, ?, ?)";
                try (PreparedStatement pstHoaDon = connection.prepareStatement(queryHoaDon, Statement.RETURN_GENERATED_KEYS)) {
                    int customerId = getCustomerId(tenKhachHang, soDienThoai);

                    pstHoaDon.setInt(1, this.currentUserId);
                    pstHoaDon.setDate(2, new java.sql.Date(System.currentTimeMillis()));
                    pstHoaDon.setString(3, "Chưa thanh toán");
                    pstHoaDon.setInt(4, customerId);
                    pstHoaDon.setString(5, voucherCode);
                    pstHoaDon.setDouble(6, VAT * 100);
                    pstHoaDon.setString(7, phuongThucThanhToan);
                    pstHoaDon.setDouble(8, tongTienSauVoucher);
                    pstHoaDon.setDate(9, new java.sql.Date(System.currentTimeMillis()));

                    int affectedRows = pstHoaDon.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("Lưu trữ hóa đơn thất bại, không có hàng nào bị ảnh hưởng.");
                    }

                    try (ResultSet generatedKeys = pstHoaDon.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            long maHoaDonMoi = generatedKeys.getLong(1);
                            insertOrderDetails(maHoaDonMoi); // Thêm chi tiết đơn hàng
                        } else {
                            throw new SQLException("Lưu trữ hóa đơn thất bại, không có ID được lấy.");
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Lưu trữ hóa đơn thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lưu trữ hóa đơn thất bại: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        resetInvoiceData();
        tblDSHD.setModel(qlHoaDon.loadOrderData());
        ql.LoadDSSPTrongQLBH(tblDSSanPham);
        ql.DanhSachHoaDonCho(tblHoaDonCho);
        isContinuingPayment = false;
    } catch (NumberFormatException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi định dạng số: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException ex) {
        Logger.getLogger(View_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    
    private void txtMaCTSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaCTSPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaCTSPActionPerformed

    private void btnKhachLeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhachLeActionPerformed
           for (int i = 0; i < tblKhachH.getRowCount(); i++) {
        String sdtTrongBang = tblKhachH.getValueAt(i, 3).toString();
        if (sdtTrongBang.equals("0000000000")) {
            String tenKhachHang = tblKhachH.getValueAt(i, 2).toString();
            txtTenKhachHang.setText(tenKhachHang);
            txtSDTKhachHang.setText(sdtTrongBang);
            break;
        }
    }
           tblHoaDonCho.clearSelection();
    }//GEN-LAST:event_btnKhachLeActionPerformed

    private void btnQuetMaQRCodeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuetMaQRCodeeActionPerformed
   qrCode.toggleScanning();
    
    qrCode.setOnScanResultListener((productDetailCode) -> {
        // Tìm dòng sản phẩm trong bảng tblDSSanPham theo productDetailCode
        DefaultTableModel modelSanPham = (DefaultTableModel) tblDSSanPham.getModel();
        int selectedRow = -1;
        for (int i = 0; i < modelSanPham.getRowCount(); i++) {
            if (modelSanPham.getValueAt(i, 1).toString().equals(productDetailCode)) {
                selectedRow = i;
                break;
            }
        }

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với mã QR đã quét", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int soLuongNhapVao = 1; // Số lượng mặc định là 1

            // Kiểm tra số lượng hiện có
            int soLuongHienCo = Integer.parseInt(tblDSSanPham.getValueAt(selectedRow, 7).toString()); // Cột số lượng ở vị trí thứ 7
            double giaBan = Double.parseDouble(tblDSSanPham.getValueAt(selectedRow, 8).toString()); // Cột giá bán ở vị trí thứ 8

            if (soLuongNhapVao > soLuongHienCo) {
                JOptionPane.showMessageDialog(this, "Số lượng nhập vào vượt quá số lượng hiện có", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lấy thông tin sản phẩm từ bảng tblDSSanPham
            String tenSP = tblDSSanPham.getValueAt(selectedRow, 2).toString();
            String loaiSP = tblDSSanPham.getValueAt(selectedRow, 3).toString();
            String mauSac = tblDSSanPham.getValueAt(selectedRow, 4).toString();
            String kichThuoc = tblDSSanPham.getValueAt(selectedRow, 5).toString();
            String phongCach = tblDSSanPham.getValueAt(selectedRow, 6).toString();

            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            DefaultTableModel modelTrongHD = (DefaultTableModel) tblDSSPtrongHD.getModel();
            boolean found = false;
            int stt = modelTrongHD.getRowCount() + 1; // STT là số thứ tự của hàng mới

            for (int i = 0; i < modelTrongHD.getRowCount(); i++) {
                if (modelTrongHD.getValueAt(i, 1).toString().equals(productDetailCode)) { // Kiểm tra mã SPCT
                    int soLuongHienCoTrongHD = Integer.parseInt(modelTrongHD.getValueAt(i, 7).toString());
                    int soLuongMoiTrongHD = soLuongHienCoTrongHD + soLuongNhapVao;
                    modelTrongHD.setValueAt(soLuongMoiTrongHD, i, 7); // Cập nhật lại số lượng trong giỏ hàng
                    found = true;
                    break;
                }
            }

            if (!found) {
                // Thêm sản phẩm mới vào giỏ hàng nếu chưa có
                modelTrongHD.addRow(new Object[]{stt, productDetailCode, tenSP, loaiSP, mauSac, kichThuoc, phongCach, soLuongNhapVao, giaBan});
            }

            // Cập nhật số lượng sản phẩm trong bảng tblDSSanPham
            int soLuongMoi = soLuongHienCo - soLuongNhapVao;
            tblDSSanPham.setValueAt(soLuongMoi, selectedRow, 7); // Cập nhật lại cột số lượng
            danhSachSanPhamDaChon.add(new Object[]{stt, productDetailCode, tenSP, loaiSP, mauSac, kichThuoc, phongCach, soLuongNhapVao, giaBan});
            tinhTongTien();
            CapNhatGiaKhiChonVoucher();
            txtNhapSoLuong.setText("");
            lamMoi();

        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Dữ liệu trong bảng không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    });

    }//GEN-LAST:event_btnQuetMaQRCodeeActionPerformed

        private int findOrderIdByCode(String orderCode) {
    // Tìm mã đơn hàng từ mã hóa đơn
    int orderId = 0;
    String query = "SELECT order_id FROM Orders WHERE order_code = ?";
    try (Connection connection = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456").getConnection();
         PreparedStatement pst = connection.prepareStatement(query)) {

        pst.setString(1, orderCode);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                orderId = rs.getInt("order_id");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return orderId;
}
  
        private void tblDSHDFocusLost(java.awt.event.FocusEvent evt) {
    // Nếu bảng tblDSHD bị mất focus, xóa dòng được chọn
    tblDSHD.clearSelection();
    // Xóa dữ liệu trong tblSanPhamTrongHoaDon
    DefaultTableModel model = (DefaultTableModel) tblSanPhamTrongHoaDon.getModel();
    model.setRowCount(0);
}
        
    private void tblDSHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSHDMouseClicked
  int row = tblDSHD.getSelectedRow();
    if (row >= 0) {
        // Lấy mã đơn hàng từ bảng tblDSHD
        String orderCode = (String) tblDSHD.getValueAt(row, 1);
        
        // Tìm mã đơn hàng từ mã hóa đơn
        int orderId = findOrderIdByCode(orderCode);
        
        // Lấy danh sách chi tiết sản phẩm cho đơn hàng
        ArrayList<OrderProductDetails> productDetails = (ArrayList<OrderProductDetails>) qlHoaDon.getOrderDetails(orderId);
        
        DefaultTableModel model = (DefaultTableModel) tblSanPhamTrongHoaDon.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ
        int stt = 1;
        for (OrderProductDetails detail : productDetails) {
            model.addRow(new Object[]{
                stt,
                detail.getProductDetailCode(),
                detail.getProductName(),
                detail.getProductType(),
                detail.getColor(),
                detail.getSize(),
                detail.getStyle(),
                detail.getQuantity(),
                detail.getUnitPrice()
            });
            stt++;
        }
    }
    }//GEN-LAST:event_tblDSHDMouseClicked



    private void cbbLocTrangThaiHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbLocTrangThaiHoaDonActionPerformed
      String selectedStatus = (String) cbbLocTrangThaiHoaDon.getSelectedItem();
    tblDSHD.setModel(qlHoaDon.loadOrderDataByStatus(selectedStatus));
    }//GEN-LAST:event_cbbLocTrangThaiHoaDonActionPerformed

    private void txtLocHoaDonTheoMaHDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLocHoaDonTheoMaHDKeyReleased
    String orderCode = txtLocHoaDonTheoMaHD.getText();
    tblDSHD.setModel(qlHoaDon.loadOrderDataByOrderCode(orderCode));
    }//GEN-LAST:event_txtLocHoaDonTheoMaHDKeyReleased

    private void btnXuatExcelHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatExcelHoaDonActionPerformed
//    ArrayList<Order> dsOrder = (ArrayList<Order>) qlHoaDon.getAllOrders();
//    JFileChooser fileChooser = new JFileChooser();   
//    // Đặt kiểu chọn là chỉ chọn file
//    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);    
//    // Đặt tiêu đề cho hộp thoại
//    fileChooser.setDialogTitle("Chọn nơi lưu tệp");
//    // Đặt bộ lọc để chỉ hiển thị các file có đuôi .xlsx
//    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx"));
//    // Hiển thị hộp thoại lưu và lấy kết quả
//    int userSelection = fileChooser.showSaveDialog(this);    
//    if (userSelection == JFileChooser.APPROVE_OPTION) {
//        // Lấy file đã chọn
//        File fileToSave = fileChooser.getSelectedFile();      
//        // Đảm bảo file có đuôi .xlsx
//        String fileName = fileToSave.getAbsolutePath();
//        if (!fileName.endsWith(".xlsx")) {
//            fileName += ".xlsx";
//        }
//        // Xuất file Excel
//        ex.xuatHoaDon(dsOrder, fileName);
//        JOptionPane.showMessageDialog(this, "Xuất danh sách hóa đơn ra tệp " + fileName + " thành công.");
//    }     

    
    int rowCount = tblDSHD.getRowCount();
ArrayList<Order> dsOrder = new ArrayList<>();

SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng ngày tháng bạn muốn

for (int i = 0; i < rowCount; i++) {
    Order order = new Order();
    order.setOrderCode(tblDSHD.getValueAt(i, 1).toString());
    order.setUserCode(tblDSHD.getValueAt(i, 2).toString());
    order.setUserName(tblDSHD.getValueAt(i, 3).toString());
    order.setCustomerCode(tblDSHD.getValueAt(i, 4).toString());
    order.setCustomerName(tblDSHD.getValueAt(i, 5).toString());
    
    // Lấy giá trị ngày và chuyển đổi thành java.util.Date
    String dateString = tblDSHD.getValueAt(i, 6).toString();
        try {
            java.util.Date orderDate = dateFormat.parse(dateString);
            // Chuyển đổi thành String nếu cần
            String formattedDate = dateFormat.format(orderDate);
            order.setOrderDate(formattedDate); // Nếu setOrderDate yêu cầu String
        } catch (ParseException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu không thể chuyển đổi ngày
        }
    
    // Kiểm tra voucher và gán giá trị
    Object voucherValue = tblDSHD.getValueAt(i, 7);
    order.setVoucherCode(voucherValue != null ? voucherValue.toString() : ""); // Gán chuỗi trống nếu voucher là null

    order.setVat(Double.parseDouble(tblDSHD.getValueAt(i, 8).toString()));
    order.setPaymentMethod(tblDSHD.getValueAt(i, 9).toString());
    order.setTotalPrice(Double.parseDouble(tblDSHD.getValueAt(i, 10).toString()));
    order.setStatus(tblDSHD.getValueAt(i, 11).toString());
    
    dsOrder.add(order);
}

JFileChooser fileChooser = new JFileChooser();   
fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);    
fileChooser.setDialogTitle("Chọn nơi lưu tệp");
fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx"));

int userSelection = fileChooser.showSaveDialog(this);    
if (userSelection == JFileChooser.APPROVE_OPTION) {
    File fileToSave = fileChooser.getSelectedFile();      
    String fileName = fileToSave.getAbsolutePath();
    if (!fileName.endsWith(".xlsx")) {
        fileName += ".xlsx";
    }
    
    ex.xuatHoaDon(dsOrder, fileName);
    JOptionPane.showMessageDialog(this, "Xuất danh sách hóa đơn ra tệp " + fileName + " thành công.");
}
    }//GEN-LAST:event_btnXuatExcelHoaDonActionPerformed

    private void btnLocTheoKhoangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocTheoKhoangActionPerformed
     java.util.Date startDate = dateTu.getDate();
    java.util.Date endDate = dateDen.getDate();
    
    if (startDate != null && endDate != null) {
        tblDSHD.setModel(qlHoaDon.loadOrderDataByDateRange(startDate, endDate));
    } else {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn khoảng ngày hợp lệ.");
    }
    }//GEN-LAST:event_btnLocTheoKhoangActionPerformed

    private void tblDanhSachDMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachDMMouseClicked
        // TODO add your handling code here:
        txtMaDM.setEnabled(false);
        txtTenDM.setEnabled(false);
        index = tblDanhSachDM.getSelectedRow();
        try {
            showDetails();
        } catch (SQLDataException ex) {
            Logger.getLogger(View_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tblDanhSachDMMouseClicked

    private void txtMaDMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaDMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaDMActionPerformed

    private void txtTenDMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenDMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenDMActionPerformed

    private void txtHoTenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoTenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoTenActionPerformed

    private void btnCapNhatThongTinCNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatThongTinCNActionPerformed

        String hoTen = this.txtHoTen.getText();
        String userName = this.txtUsername.getText();
        String email = this.txtEmail.getText();
        String sdt = this.txtSdt.getText();
        String diaChi = this.txtDiaChi.getText();
        String vaiTro = txtVaiTro.getText();
        int idUser = this.currentUserId;
        // Mở form cập nhật thông tin người dùng
        new FormUpdateThongTin(this, true, idUser, hoTen, userName, email, sdt, diaChi, vaiTro).setVisible(true);
        try {
            DatabaseConnectionManager dcm = new DatabaseConnectionManager("QuanLyShirtService", "sa", "123456");
            Connection connection = dcm.getConnection();
            String sql = "SELECT hoTen, username, email, phone, [address], role_id FROM Users WHERE user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtHoTen.setText(rs.getString("hoTen")); // Hiển thị tên người dùng
                txtUsername.setText(rs.getString("username"));
                txtEmail.setText(rs.getString("email"));
                txtSdt.setText(rs.getString("phone"));
                txtDiaChi.setText(rs.getString("address"));

                int roleId = rs.getInt("role_id");
                String roleName = ql.getRoleName(roleId); // Lấy tên vai trò từ role_id
                txtVaiTro.setText(roleName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCapNhatThongTinCNActionPerformed

    private void btnDoiMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoiMatKhauActionPerformed
        try {
      
                     
        this.jpViewQLHD.setVisible(false);
        this.jpViewQLDM.setVisible(false);
        this.jpViewQLKH.setVisible(false);
        this.jpViewQLKM.setVisible(false);
        this.jpViewQLSPP.setVisible(false);
        this.jpViewQLTK.setVisible(false);
        this.jpViewTK.setVisible(false);
        this.jpViewQLBH2.setVisible(false);       
        this.jpViewDoiMK.setVisible(true); 
        this.jpViewQLSPP2.setVisible(false);
        this.jpViewQLSPP3.setVisible(false);
            
            
            // code them
            Users1 us1 = us1sv.getUser1(this.currentUserId);
            txtUsername1.setText(us1.getUsername());

        } catch (SQLDataException ex) {
            Logger.getLogger(View_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDoiMatKhauActionPerformed

    private void tabQLNVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabQLNVMouseClicked
        // TODO add your handling code here:\

    }//GEN-LAST:event_tabQLNVMouseClicked

    private void btnXacNhanDMKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanDMKActionPerformed

       
      // code thêm 
        String userName = txtUsername1.getText();
        String passWord = txtPassword.getText();
        String newPass = txtNewPassword.getText();
        String confirmPass = txtConfirmPassword.getText();
        if (us1sv.validateDoiMk(txtUsername1, txtPassword, txtNewPassword, txtConfirmPassword) == true) {
            if (!newPass.equals(confirmPass)) {
                this.jpViewQLHD.setVisible(false);
                 this.jpViewQLDM.setVisible(false);
                 this.jpViewQLKH.setVisible(false);
                this.jpViewQLKM.setVisible(false);
                this.jpViewQLSPP.setVisible(false);
                this.jpViewQLTK.setVisible(false);
                this.jpViewTK.setVisible(false);
                this.jpViewQLBH2.setVisible(false);    
                  this.jpViewDoiMK.setVisible(true);
                this.jpViewQLSPP2.setVisible(false);
         this.jpViewQLSPP3.setVisible(false); 
                JOptionPane.showMessageDialog(this, "Mật khẩu mới không khớp");
                
                return;
            }
            boolean kq = us1sv.doiMk(userName, passWord, newPass);
            if (kq) {
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công");
                  this.jpViewQLHD.setVisible(false);
                 this.jpViewQLDM.setVisible(false);
                 this.jpViewQLKH.setVisible(false);
                this.jpViewQLKM.setVisible(false);
                this.jpViewQLSPP.setVisible(false);
                this.jpViewQLTK.setVisible(true);
                this.jpViewTK.setVisible(false);
                this.jpViewQLBH2.setVisible(false);    
                  this.jpViewDoiMK.setVisible(false);
                this.jpViewQLSPP2.setVisible(false);
         this.jpViewQLSPP3.setVisible(false); 
                txtPassword.setText("");
                txtNewPassword.setText("");
                txtConfirmPassword.setText("");

            } else {
                 this.jpViewQLHD.setVisible(false);
                 this.jpViewQLDM.setVisible(false);
                 this.jpViewQLKH.setVisible(false);
                this.jpViewQLKM.setVisible(false);
                this.jpViewQLSPP.setVisible(false);
                this.jpViewQLTK.setVisible(false);
                this.jpViewTK.setVisible(false);
                this.jpViewQLBH2.setVisible(false);    
                  this.jpViewDoiMK.setVisible(true);
                this.jpViewQLSPP2.setVisible(false);
         this.jpViewQLSPP3.setVisible(false);
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại");

            }
        }
    }//GEN-LAST:event_btnXacNhanDMKActionPerformed

    private void btnQuaylaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnQuaylaiMouseClicked
        // TODO add your handling code here:
       this.jpViewQLHD.setVisible(false);
    this.jpViewQLDM.setVisible(false);
    this.jpViewQLKH.setVisible(false);
    this.jpViewQLKM.setVisible(false);
    this.jpViewQLSPP.setVisible(false);
    this.jpViewQLTK.setVisible(true);
    this.jpViewTK.setVisible(false);
    this.jpViewQLBH2.setVisible(false);    
    this.jpViewDoiMK.setVisible(false);
    this.jpViewQLSPP2.setVisible(false);
    this.jpViewQLSPP3.setVisible(false); 

        this.jpQLBH.setBackground(new Color(255, 102, 0));
        this.jpQLDM.setBackground(new Color(255, 102, 0));
        this.jpQLKH.setBackground(new Color(255, 102, 0));
        this.jpQLKM.setBackground(new Color(255, 102, 0));
        this.jpQLSP.setBackground(new Color(255, 102, 0));
        this.jpQLTK.setBackground(new Color(220, 220, 220));
        this.jpTK.setBackground(new Color(255, 102, 0));

    }//GEN-LAST:event_btnQuaylaiMouseClicked

    private void btnQuaylaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuaylaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnQuaylaiActionPerformed

    private void btnEyeMKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEyeMKActionPerformed
        // TODO add your handling code here:
        if (trangThaiMk) {
            txtPassword.setEchoChar('•');  // Ẩn mật khẩu
        } else {
            txtPassword.setEchoChar((char) 0);  // Hiển thị mật khẩu
        }
        trangThaiMk = !trangThaiMk;
    }//GEN-LAST:event_btnEyeMKActionPerformed

    private void btnEyeMKMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEyeMKMoiActionPerformed
        // TODO add your handling code here:
        if (trangThaiMk) {
            txtNewPassword.setEchoChar('•');  // Ẩn mật khẩu
        } else {
            txtNewPassword.setEchoChar((char) 0);  // Hiển thị mật khẩu
        }
        trangThaiMk = !trangThaiMk;
    }//GEN-LAST:event_btnEyeMKMoiActionPerformed

    private void btnEyeXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEyeXacNhanActionPerformed
        // TODO add your handling code here:
        if (trangThaiMk) {
            txtConfirmPassword.setEchoChar('•');  // Ẩn mật khẩu
        } else {
            txtConfirmPassword.setEchoChar((char) 0);  // Hiển thị mật khẩu
        }
        trangThaiMk = !trangThaiMk;
    }//GEN-LAST:event_btnEyeXacNhanActionPerformed

    private void btnAddProductDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductDetailsActionPerformed
        this.jpViewQLBH2.setVisible(false);
        this.jpViewQLHD.setVisible(false);
        this.jpViewQLDM.setVisible(false);
        this.jpViewQLKH.setVisible(false);
        this.jpViewQLKM.setVisible(false);
        this.jpViewQLSPP.setVisible(false);
        this.jpViewQLTK.setVisible(false);
        this.jpViewTK.setVisible(false);
        this.jpViewQLSPP2.setVisible(true);
        this.jpViewQLSPP3.setVisible(false);
        this.jpViewDoiMK.setVisible(false);

        this.isAddOrUpdate = false;
        this.setEnableFormSP();
        this.setInputFields("sản phẩm chi tiết");
        this.setInputFields("sản phẩm");

        int[] selectedRows = this.tblHienThiKetQua.getSelectedRows();

        if (selectedRows.length == 0) {
            this.qlsp.loadTableDetail(tblSPCT, this.qlsp.getAllProductDetail(), currentPage, rowsPerPage);
            currentPage = 1;
            updatePagination(qlsp.getAllProductDetail());
            this.fillter = false;
        } else {
            ArrayList<ProductDetails> list = new ArrayList<>();
            for (int rowIndex : selectedRows) {
                String productCode = (String) this.tblHienThiKetQua.getValueAt(rowIndex, 1);
                list.addAll(this.qlsp.getListById(productCode));
                this.fillter = true;
            }
            this.qlsp.loadTableDetail(tblSPCT, list, currentPage, rowsPerPage);
            currentPage = 1;
            updatePagination(list);
        }
    }//GEN-LAST:event_btnAddProductDetailsActionPerformed

    private void tblHienThiKetQuaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHienThiKetQuaMouseClicked
        this.setEnableFormSP();
        int rowIndex = this.tblHienThiKetQua.getSelectedRow();
        if (rowIndex == -1 || rowIndex >= this.tblHienThiKetQua.getRowCount()) {
            return;
        }

        Products pro = this.qlsp.getAllProduct().get(rowIndex);
        this.txtMaSanPham.setText(pro.getProduct_code());
        this.txtTenSP.setText(pro.getProduct_name());
        this.txtMotaSPP.setText(pro.getDescription());
        this.cbbDanhMuc.setSelectedItem(pro.getCategory().getCategory_name());
        this.cbbNhaCC.setSelectedItem(pro.getSupplier().getSupplier_name());
        this.rdoRun.setSelected(pro.getStatus());
        this.rdoStop.setSelected(!pro.getStatus());

        this.isAddOrUpdate = false;
    }//GEN-LAST:event_tblHienThiKetQuaMouseClicked

    private void txtTimKiemTheoMaCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtTimKiemTheoMaCaretUpdate
        String keyword = this.txtTimKiemTheoMa.getText().trim().toUpperCase();
        this.qlsp.loadTable(this.tblHienThiKetQua, this.qlsp.GetListSearch(keyword), currentPages, rowsPerPage);
    }//GEN-LAST:event_txtTimKiemTheoMaCaretUpdate

    private void txtTimKiemTheoMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemTheoMaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemTheoMaActionPerformed

    private void txtTimKiemTheoMaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemTheoMaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemTheoMaKeyPressed

    private void btnHoanTac1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoanTac1ActionPerformed
        this.cbbLocTheoDM.setSelectedItem("");
        this.cbbTrangThaiSP.setSelectedItem("");
        this.cbbLocNhaCC.setSelectedItem("");
        this.qlsp.loadTable(this.tblHienThiKetQua, this.qlsp.getAllProduct(), currentPages, rowsPerPage);
    }//GEN-LAST:event_btnHoanTac1ActionPerformed

    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
        ArrayList<Products> productList = (ArrayList<Products>) qlsp.getAllProduct();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu tệp");

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel files", "xlsx"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            ec.exportProductListToExcel(productList, filePath);
            JOptionPane.showMessageDialog(this, "Xuất danh sách sản phẩm ra tệp " + filePath + " thành công.");
        }
    }//GEN-LAST:event_btnExcelActionPerformed

    private void btnPreviousAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousAllActionPerformed
        currentPages = 1;
        updatePaginations(qlsp.filterProducts(selectedCategory, selectedSupplier, selectedStatus));
    }//GEN-LAST:event_btnPreviousAllActionPerformed

    private void btnPreviouActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviouActionPerformed
        if (currentPages > 1) {
            currentPages--;
            updatePaginations(qlsp.filterProducts(selectedCategory, selectedSupplier, selectedStatus));
        }
    }//GEN-LAST:event_btnPreviouActionPerformed

    private void btnNexttActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNexttActionPerformed
        if (currentPages < totalPage) {
            currentPages++;
            updatePaginations(qlsp.filterProducts(selectedCategory, selectedSupplier, selectedStatus));
        }
    }//GEN-LAST:event_btnNexttActionPerformed

    private void btnNexttALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNexttALLActionPerformed
        currentPages = totalPage;
        updatePaginations(qlsp.filterProducts(selectedCategory, selectedSupplier, selectedStatus));
    }//GEN-LAST:event_btnNexttALLActionPerformed

    private void btnBackProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackProductsActionPerformed
        this.jpViewQLBH2.setVisible(false);
        this.jpViewQLHD.setVisible(false);
        this.jpViewQLDM.setVisible(false);
        this.jpViewQLKH.setVisible(false);
        this.jpViewQLKM.setVisible(false);
        this.jpViewQLSPP.setVisible(true);
        this.jpViewQLTK.setVisible(false);
        this.jpViewTK.setVisible(false);
        this.jpViewQLSPP2.setVisible(false);
        this.jpViewQLSPP3.setVisible(false);
        this.jpViewDoiMK.setVisible(false);

        this.isAddOrUpdate = false;
        this.setEnableFormSPCT();
        this.qlsp.loadTable(tblHienThiKetQua, this.qlsp.getAllProduct(), currentPages, rowsPerPage);
        currentPages = 1;
        updatePaginations(qlsp.getAllProduct());
        this.qlsp.addCbbNhaCC(cbbNhaCC);
        this.qlsp.addCbbNhaCC(cbbLocNhaCC);
    }//GEN-LAST:event_btnBackProductsActionPerformed

    private void btnThemMoiThuocTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemMoiThuocTinhActionPerformed
        this.jpViewQLBH2.setVisible(false);
        this.jpViewQLHD.setVisible(false);
        this.jpViewQLDM.setVisible(false);
        this.jpViewQLKH.setVisible(false);
        this.jpViewQLKM.setVisible(false);
        this.jpViewQLSPP.setVisible(false);
        this.jpViewQLTK.setVisible(false);
        this.jpViewTK.setVisible(false);
        this.jpViewQLSPP2.setVisible(false);
        this.jpViewQLSPP3.setVisible(true);
        this.jpViewDoiMK.setVisible(false);

        this.isAddOrUpdate = false;
        this.setEnableFormSPCT();
    }//GEN-LAST:event_btnThemMoiThuocTinhActionPerformed

    private void lblImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblImageMouseClicked
        if (!this.isAddOrUpdate) {
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String imagePath = file.getAbsolutePath();

            displayImage(imagePath);
            this.image = imagePath;
        }
    }//GEN-LAST:event_lblImageMouseClicked

    private void txtSearhProductDetailCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtSearhProductDetailCaretUpdate
        String keyword = this.txtSearhProductDetail.getText();
        this.qlsp.loadTableDetail(tblSPCT, this.qlsp.GetListSearchProductDetail(keyword), currentPage, rowsPerPage);
    }//GEN-LAST:event_txtSearhProductDetailCaretUpdate

    private void btnXemDSSPCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXemDSSPCTActionPerformed

        this.qlsp.loadTableDetail(tblSPCT, this.qlsp.getAllProductDetail(), currentPage, rowsPerPage);
        this.cbbLoaiSPCT.setSelectedItem("");
        this.cbbStyles.setSelectedItem("");
        this.cbbKichThuocSPCT.setSelectedItem("");
        this.cbbMauSacSPCT.setSelectedItem("");
        this.cbbSoLuong.setSelectedItem("");
        this.cbbGiaBan.setSelectedItem("");
    }//GEN-LAST:event_btnXemDSSPCTActionPerformed

    private void btnQRcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQRcodeActionPerformed
        qrCodeScanner.toggleScanning();
    }//GEN-LAST:event_btnQRcodeActionPerformed

    private void tblSPCTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSPCTMouseClicked
        this.setEnableFormSPCT();
        int rowIndex = this.tblSPCT.getSelectedRow();
        if (rowIndex == -1 || rowIndex >= this.tblSPCT.getRowCount()) {
            return;
        }

        ProductDetails proDe = this.qlsp.getAllProductDetail().get(rowIndex);
        this.txtMaSPCT.setText(proDe.getProductDetail_code());
        this.cbbStyle.setSelectedItem(proDe.getStyle());
        this.txtMaSP.setText(proDe.getProduct_code());
        this.cbbColor.setSelectedItem(proDe.getColor());
        this.cbbSizes.setSelectedItem(proDe.getSize());
        this.txtQuantity.setText(String.valueOf(proDe.getQuantity()));
        this.txtPrice.setText(String.valueOf(proDe.getPrice()));

        String imagePath = proDe.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            displayImage(imagePath);
        } else {
            lblImage.setIcon(null);
            lblImage.setText("Không có hình ảnh");
        }
        this.isAddOrUpdate = false;
    }//GEN-LAST:event_tblSPCTMouseClicked

    private void cbbSoLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbSoLuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbSoLuongActionPerformed

    private void btnExcelProductDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelProductDetailActionPerformed
        ArrayList<ProductDetails> productDetailList = (ArrayList<ProductDetails>) qlsp.getAllProductDetail();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu tệp");

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel files", "xlsx"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            ec.exportProductDetailListToExcel(productDetailList, filePath);
            JOptionPane.showMessageDialog(this, "Xuất danh sách sản phẩm chi tiết ra tệp " + filePath + " thành công.");
        }
    }//GEN-LAST:event_btnExcelProductDetailActionPerformed

    private void btnDowloadQrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDowloadQrActionPerformed
        int rowIndex = this.tblSPCT.getSelectedRow();
        if (rowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 sản phẩm để thực hiện dowload QR_CODE.");
            return;
        }

        String productDetailCode = (String) this.tblSPCT.getValueAt(rowIndex, 1);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            String filePath = selectedFolder.getAbsolutePath() + "/QRCode_" + productDetailCode + ".png";

            try {
                QRCodeGenerator.generateQRCode(productDetailCode, filePath);
                JOptionPane.showMessageDialog(this, "Mã QR đã được lưu thành công tại: " + filePath);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi tạo mã QR.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Bạn đã hủy chọn thư mục lưu.");
        }
    }//GEN-LAST:event_btnDowloadQrActionPerformed

    private void btnPreviouAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviouAllActionPerformed
        navigatePage(1, fillter);
    }//GEN-LAST:event_btnPreviouAllActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        if (currentPage > 1) {
            navigatePage(currentPage - 1, fillter);
        }
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        if (currentPage < totalPages) {
            navigatePage(currentPage + 1, fillter);
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnNextAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextAllActionPerformed
        navigatePage(totalPages, fillter);
    }//GEN-LAST:event_btnNextAllActionPerformed

    private void tblMauSacMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMauSacMouseClicked
        this.setEnableFormColor(false);
        int rowIndex = this.tblMauSac.getSelectedRow();
        if (rowIndex == -1) {
            return;
        }

        Model.Color color = qlsp.getAllColor().get(rowIndex);
        this.txtMaMau.setText(color.getColor_id());
        this.txtTenMau.setText(color.getColor_name());
    }//GEN-LAST:event_tblMauSacMouseClicked

    private void tblKichThuocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKichThuocMouseClicked
        this.setEnableFormSize(false);
        int rowIndex = this.tblKichThuoc.getSelectedRow();
        if (rowIndex == -1) {
            return;
        }

        Size size = qlsp.getAllSize().get(rowIndex);
        this.txtMaKT.setText(size.getSize_id());
        this.txtTenKichThuoc.setText(size.getSize_name());
        this.txtMoTaSize.setText(size.getSize_description());
    }//GEN-LAST:event_tblKichThuocMouseClicked

    private void tblNhaCungCapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhaCungCapMouseClicked
        this.setEnableFormSupp(false);
        int rowIndex = this.tblNhaCungCap.getSelectedRow();
        if (rowIndex == -1) {
            return;
        }

        Suppliers supp = qlsp.getAllSuppliers().get(rowIndex);
        this.txtNCC.setText(supp.getSupplier_id());
        this.txtTenNCC.setText(supp.getSupplier_name());
        this.txtSDTNCC.setText(supp.getPhone());
        this.txtEmailNCC.setText(supp.getEmail());
        this.txtDiaChiNCC.setText(supp.getAddress());
    }//GEN-LAST:event_tblNhaCungCapMouseClicked

    private void tblPhongCachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPhongCachMouseClicked
        this.setEnableFormStyle(false);
        int rowIndex = this.tblPhongCach.getSelectedRow();
        if (rowIndex == -1) {
            return;
        }

        Style style = qlsp.getAllStyle().get(rowIndex);
        this.txtMaPC.setText(style.getStyle_id());
        this.txtTenPhongCach.setText(style.getStyle_name());
        this.txtMoTaPC.setText(style.getStyle_description());
    }//GEN-LAST:event_tblPhongCachMouseClicked

    private void btnBackkkkkkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackkkkkkActionPerformed
        this.jpViewQLBH2.setVisible(false);
        this.jpViewQLHD.setVisible(false);
        this.jpViewQLDM.setVisible(false);
        this.jpViewQLKH.setVisible(false);
        this.jpViewQLKM.setVisible(false);
        this.jpViewQLSPP.setVisible(false);
        this.jpViewQLTK.setVisible(false);
        this.jpViewTK.setVisible(false);
        this.jpViewQLSPP2.setVisible(true);
        this.jpViewQLSPP3.setVisible(false);
        this.jpViewDoiMK.setVisible(false);

        this.setInputFields("màu sắc");
        this.setInputFields("kích thước");
        this.setInputFields("nhà cung cấp");
        this.setInputFields("phong cách");
        this.setEnableFormColor(false);
        this.setEnableFormSize(false);
        this.setEnableFormStyle(false);
        this.setEnableFormSupp(false);
        this.isAddOrUpdate = false;
        this.qlsp.addCbbColor(cbbColor);
        this.qlsp.addCbbColor(cbbMauSacSPCT);
        this.qlsp.addCbbSize(cbbSizes);
        this.qlsp.addCbbSize(cbbKichThuocSPCT);
        this.qlsp.addCbbStyle(cbbStyles);
        this.qlsp.addCbbStyle(cbbStyle);
    }//GEN-LAST:event_btnBackkkkkkActionPerformed

    private void tblKhachHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhachHMouseClicked
        Integer rowIndex = this.tblKhachH.getSelectedRow();
        if (rowIndex == -1) {
            return;
        }

        Customers cus = this.qlkh.getAllCustomer().get(rowIndex);
        this.txtMaKH.setText(cus.getCustomer_code());
        this.txtITenKhachHang1.setText(cus.getCustomer_name());
        this.txtSoDiienThoai.setText(cus.getPhone());
        this.txtDiemTichLuy.setText(String.valueOf(cus.getPoint_gained()));
        this.txtDiemDaDung.setText(String.valueOf(cus.getPoints_used()));
        this.txtTinhDiem.setText(String.valueOf(cus.getPoint_gained() - cus.getPoints_used()));

        this.setEnableFormKH();
        this.isAddOrUpdate = true;
    }//GEN-LAST:event_tblKhachHMouseClicked

    private void btnThemMoiKH1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemMoiKH1ActionPerformed
        this.tblKhachH.clearSelection();
        this.setText();
        this.txtITenKhachHang1.setEnabled(true);
        this.txtSoDiienThoai.setEnabled(true);

        this.choice = true;
        this.isAddOrUpdate = true;
    }//GEN-LAST:event_btnThemMoiKH1ActionPerformed

    private void btnLamMoiKH1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiKH1ActionPerformed
        if (!this.isAddOrUpdate) {
            return;
        }

        String cusName = this.txtITenKhachHang1.getText().trim();
        String cusPhone = this.txtSoDiienThoai.getText().trim();
        Integer cusID = this.qlkh.getCusIdByCode(this.txtMaKH.getText());

        if (cusName == null || cusName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng.");
            return;
        }
        if (cusName.matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(this, "Tên khách hàng không được chứa số.");
            return;
        }

        if (cusPhone == null || cusPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại khách hàng.");
            return;
        }
        String phonePattern = "^(03|07|08|09)[0-9]{8}$";
        if (!cusPhone.matches(phonePattern)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không đúng định dạng.");
            return;
        }

        if (this.choice && this.isAddOrUpdate) {
            if (this.qlkh.isDulicatePhoneKH(cusPhone)) {
                JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn thêm khách hàng mới?",
                "Xác nhận thêm",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Customers cus = new Customers(cusName, cusPhone);
                this.qlkh.addCus(cus);
                JOptionPane.showMessageDialog(this, "Thêm khách hàng mới thành công.");
            }

        } else if (!this.choice && this.isAddOrUpdate) {
            Integer rowIndex = this.tblKhachH.getSelectedRow();
            if (rowIndex == -1) {
                return;
            }
            Customers oldCus = qlkh.getCustomerByCode(this.txtMaKH.getText());
            if (!oldCus.getCustomer_name().equalsIgnoreCase(cusName) || !oldCus.getPhone().equalsIgnoreCase(cusPhone)) {

                String oldPhone = (String) this.tblKhachH.getValueAt(rowIndex, 3);
                if (!cusPhone.equals(oldPhone)) {
                    if (this.qlkh.isDulicatePhoneKH(cusPhone)) {
                        JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại.");
                        return;
                    }
                }

                int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn cập nhật thông tin khách hàng?",
                    "Xác nhận cập nhật",
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    Customers cus = new Customers(cusID, cusName, cusPhone);
                    this.qlkh.updateCus(cus);
                    JOptionPane.showMessageDialog(this, "Cập nhật thông tin khách hàng thành công.");
                }
            }

        }
        
        this.qlkh.loadTableCus(tblKhachH, this.qlkh.getAllCustomer());
        this.setEnableFormKH();
        this.isAddOrUpdate = false;
        this.setText();
    }//GEN-LAST:event_btnLamMoiKH1ActionPerformed

    private void btnSuaKhachHang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaKhachHang1ActionPerformed
        Integer rowIndex = this.tblKhachH.getSelectedRow();
        if (rowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng 1 dòng trên table để cập nhật.");
            return;
        }

        this.txtITenKhachHang1.setEnabled(true);
        this.txtSoDiienThoai.setEnabled(true);

        this.choice = false;
        this.isAddOrUpdate = true;
    }//GEN-LAST:event_btnSuaKhachHang1ActionPerformed

    private void txtTimKiemSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemSPActionPerformed
        //        String tenSanPham = txtTimKiemSP.getText();
        //
        //        if (tenSanPham == null || tenSanPham.trim().isEmpty()) {
            //            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống");
            //            return;
            //        }
        //
        //        try {
            //            thongKeService.TimKiemQuaTenSP(tenSanPham, tblDanhSachSPdaBan);
            //        } catch (SQLException ex) {
            //            ex.printStackTrace();
            //        }
    }//GEN-LAST:event_txtTimKiemSPActionPerformed

    private void btnTimKiemSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemSPActionPerformed

        String tenSanPham = txtTimKiemSP.getText();

        //        if (tenSanPham == null || tenSanPham.trim().isEmpty()) {
            //            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống");
            //            return;
            //        }

        try {
            thongKeService.TimKiemQuaTenSP(tenSanPham, tblDanhSachSPdaBan);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnTimKiemSPActionPerformed

    private void jTuNgayPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTuNgayPropertyChange
        //        if ("date".equals(evt.getPropertyName())) {
            //            thongKeService.filterProductsByDate(jTuNgay, jDenNgay, tblDanhSachSPdaBan);
            //        }
    }//GEN-LAST:event_jTuNgayPropertyChange

    private void jDenNgayPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDenNgayPropertyChange
        //        thongKeService.filterProductsByDate(jTuNgay, jDenNgay, tblDanhSachSPdaBan);
    }//GEN-LAST:event_jDenNgayPropertyChange

    private void btnLocKhoangSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocKhoangSanPhamActionPerformed
        thongKeService.UpdateProductsTheoDate(jTuNgay, jDenNgay, tblDanhSachSPdaBan);
    }//GEN-LAST:event_btnLocKhoangSanPhamActionPerformed

    private void tblDanhSachSPdaBanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachSPdaBanMouseClicked
        int row = this.tblDanhSachSPdaBan.getSelectedRow();
        if (row != -1) { // Kiểm tra nếu có hàng được chọn
            String productName = tblDanhSachSPdaBan.getValueAt(row, 2).toString(); // Lấy tên sản phẩm từ cột thứ 2 (index 1)

            // Cập nhật JTextField với tên sản phẩm
            this.txtTimKiemSP.setText(productName);

            try {
                // Gọi phương thức từ ThongKeService để lấy tổng số lượng
                int totalQuantity = thongKeService.getTongSLSP(productName);
                lblTongSP.setText("quantity: " + totalQuantity);
                System.out.println(totalQuantity);
                // Gọi phương thức từ ThongKeService để lấy số lượng đã bán
                int soldQuantity = thongKeService.getTongSPDaBan(productName);
                lblSPDaBan.setText("Quantity sold: " + soldQuantity);
                //                System.out.println(soldQuantity);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_tblDanhSachSPdaBanMouseClicked

    private void jDTTThangPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDTTThangPropertyChange
        thongKeService.LocDSTheoThang(jDTTNam, jDTTThang, lblDoanhThuThang);
    }//GEN-LAST:event_jDTTThangPropertyChange

    private void jDTTNamPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDTTNamPropertyChange
        thongKeService.LocDSTheoThang(jDTTNam, jDTTThang, lblDoanhThuThang);
    }//GEN-LAST:event_jDTTNamPropertyChange

    private void jDateDoanhThuNgayPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateDoanhThuNgayPropertyChange
        thongKeService.LocDoanhSoTheoNgay(jDateDoanhThuNgay, lblDoanhThuNgay);
    }//GEN-LAST:event_jDateDoanhThuNgayPropertyChange

    private void TuLocTheoKhoangUpdatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_TuLocTheoKhoangUpdatePropertyChange
        //        thongKeService.updateTongDoanhSoTheoNgayThangNam(TuLocTheoKhoangUpdate, DenLocTheoKhoangUpdate, lblDoanhThuTong);
    }//GEN-LAST:event_TuLocTheoKhoangUpdatePropertyChange

    private void DenLocTheoKhoangUpdatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_DenLocTheoKhoangUpdatePropertyChange
        //        thongKeService.updateTongDoanhSoTheoNgayThangNam(TuLocTheoKhoangUpdate, DenLocTheoKhoangUpdate, lblDoanhThuTong);
    }//GEN-LAST:event_DenLocTheoKhoangUpdatePropertyChange

    private void btnLocDoanhSoUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocDoanhSoUpdateActionPerformed
        thongKeService.updateTongDoanhSoTheoNgayThangNam(TuLocTheoKhoangUpdate, DenLocTheoKhoangUpdate, lblDoanhThuTong);
    }//GEN-LAST:event_btnLocDoanhSoUpdateActionPerformed

    private void cbbTOPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbTOPActionPerformed
        if (cbbTOP.getSelectedIndex() == 1) {
            try {
                thongKeService.LoadTableTop10SPBanItNhat(tblDanhSachSPdaBan);

            } catch (SQLException ex) {
                Logger.getLogger(View_NhanVien.class
                    .getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                thongKeService.LoadTableTop10BanChayNhat(tblDanhSachSPdaBan);

            } catch (SQLException ex) {
                Logger.getLogger(View_NhanVien.class
                    .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cbbTOPActionPerformed

    private void btnLamMoiThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiThongKeActionPerformed
        lblTongSP.setText("quantity: " + 0);
        lblSPDaBan.setText("Quantity sold:" + 0);
        txtTimKiemSP.setText("");
        thongKeService.loadTongSoLuong(lblTongSP);
        thongKeService.loadTongSoLuongDaBan(lblSPDaBan);
        try {
            thongKeService.LoadTableThongKe(tblDanhSachSPdaBan);

        } catch (SQLException ex) {
            Logger.getLogger(View_NhanVien.class
                .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLamMoiThongKeActionPerformed

    private void tblCouponMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCouponMouseClicked
        //tạo và copy
        //----------------------------------------------
        //MouseClicked của bảng Voucher giảm giá
        updateVoucher = 1;
        try {
            VoucherService vou = new VoucherService();
            vou.showVoucher(tblCoupon, txtMaGiamGia, txtMoTa, txtCode, txtPhanTramChietKhau, jdc_ngay_bat_dau_voucher, jdc_ngay_ket_thuc_voucher, txt_tien_ap_dung, txt_tien_toi_da, rdoDangKM, rdoDungKM, txt_so_luong_vou);
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtMaGiamGia.setEnabled(false);
        txtMoTa.setEnabled(false);
        txtCode.setEnabled(false);
        txtPhanTramChietKhau.setEnabled(false);
        jdc_ngay_bat_dau_voucher.setEnabled(false);
        jdc_ngay_ket_thuc_voucher.setEnabled(false);
        txt_tien_ap_dung.setEnabled(false);
        txt_tien_toi_da.setEnabled(false);
        txt_so_luong_vou.setEnabled(false);
        rdoDangKM.setEnabled(false);
        rdoDungKM.setEnabled(false);
        //-----------------------------------------------
    }//GEN-LAST:event_tblCouponMouseClicked

    private void btnAddCouMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddCouMouseClicked
        //copy-----------------------------------------------------------
        //nút thêm voucher
        addVoucher = 1;
        updateVoucher = 0;
        txtMaGiamGia.setText("");
        txtCode.setText("");
        txtMoTa.setText("");
        txtPhanTramChietKhau.setText("");
        txt_so_luong_vou.setText("");
        jdc_ngay_bat_dau_voucher.setDate(null);
        jdc_ngay_ket_thuc_voucher.setDate(null);
        txtMoTa.setBackground(Color.WHITE);
        txtPhanTramChietKhau.setBackground(Color.WHITE);
        jdc_ngay_bat_dau_voucher.setBackground(Color.WHITE);
        jdc_ngay_ket_thuc_voucher.setBackground(Color.WHITE);
        txt_tien_toi_da.setBackground(Color.WHITE);
        txt_tien_ap_dung.setBackground(Color.WHITE);
        txt_tien_ap_dung.setText("");
        txt_tien_toi_da.setText("");
        try {
            VoucherService vou = new VoucherService();
//            vou.loadTableVoucher(tblCoupon);
        } catch (Exception e) {
        }
        txtMaGiamGia.setEnabled(false);
        txtMoTa.setEnabled(true);
        txtCode.setEnabled(false);
        txtPhanTramChietKhau.setEnabled(true);
        jdc_ngay_bat_dau_voucher.setEnabled(true);
        jdc_ngay_ket_thuc_voucher.setEnabled(true);
        txt_tien_ap_dung.setEnabled(true);
        txt_tien_toi_da.setEnabled(true);
        txt_so_luong_vou.setEnabled(true);
        rdoDangKM.setEnabled(true);
        rdoDungKM.setEnabled(true);
        //------------------------------------------------
    }//GEN-LAST:event_btnAddCouMouseClicked

    private void btnAddCouActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCouActionPerformed

    }//GEN-LAST:event_btnAddCouActionPerformed

    private void btnEditCouMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditCouMouseClicked
        // tạo và copy----------------------------------------------------------------------
        //--------------------Nút cập nhật bên voucher
        addVoucher = 0;
        updateVoucher = 1;
        int row = tblCoupon.getSelectedRow();
        if (row == -1) {
            updateVoucher = 0;
            JOptionPane.showMessageDialog(this, "Hãy chọn 1 chương trình để cập nhật");
            return;
        }
        txtMaGiamGia.setEnabled(false);
        txtMoTa.setEnabled(true);
        txtCode.setEnabled(false);
        txtPhanTramChietKhau.setEnabled(true);
        jdc_ngay_bat_dau_voucher.setEnabled(true);
        jdc_ngay_ket_thuc_voucher.setEnabled(true);
        txt_tien_ap_dung.setEnabled(true);
        txt_tien_toi_da.setEnabled(true);
        txt_so_luong_vou.setEnabled(true);
        rdoDangKM.setEnabled(true);
        rdoDungKM.setEnabled(true);
        //-----------------------------------------------------------------------------------------------
    }//GEN-LAST:event_btnEditCouMouseClicked

    private void btnEditCouActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditCouActionPerformed

    }//GEN-LAST:event_btnEditCouActionPerformed

    private void txtPhanTramChietKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhanTramChietKhauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhanTramChietKhauActionPerformed

    private void btnVoHHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVoHHMouseClicked
        //tạo và copy--------------------------------------------------------------
        //nút vô hiệu hóa
        int row = tblCoupon.getSelectedRow(); // Phương thức để lấy ID của voucher được chọn
        String ma_Voucher = tblCoupon.getValueAt(row, 0).toString();
        int ma = 0;
        try {
            VoucherService vou = new VoucherService();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một voucher để vô hiệu hóa");

            } else {
                ma = Integer.parseInt(ma_Voucher);
                vou.voHieuHoaVoucher(ma);
//                vou.loadTableVoucher(tblCoupon);
                JOptionPane.showMessageDialog(this, " Vô hiệu hóa thành công");
            }
        } catch (Exception e) {
            e.printStackTrace();

            return;
        }
        //-----------------------------------------------------------------------------------------
    }//GEN-LAST:event_btnVoHHMouseClicked

    private void btnLuuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLuuMouseClicked
        // TODO add your handling code here:
        //tạo và copy-------------Nút lưu Voucher--------------------------------
        int i = tblCoupon.getSelectedRow();

        if (i == -1) {
            if (addVoucher == 0) {
                return;
            }

            String mota = txtMoTa.getText().trim();
            String discountValue = txtPhanTramChietKhau.getText().trim();
            String trangThai = rdoDangKM.isSelected() ? "Hoạt động" : "Dừng hoạt động";
            String min = txt_tien_ap_dung.getText().trim();
            String max = txt_tien_toi_da.getText().trim();
            String sl = txt_so_luong_vou.getText().trim();

            try {
                VoucherService vou = new VoucherService();
                if (vou.readFromVou(txtMoTa, txtPhanTramChietKhau, jdc_ngay_bat_dau_voucher, jdc_ngay_ket_thuc_voucher, txt_tien_ap_dung, txt_tien_toi_da, txt_so_luong_vou, rdoDangKM) == null) {
                    return;
                }
                int conf = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thêm ?");
                if (conf != 0) {
                    return;
                }
                vou.themVoucher(vou.readFromVou(txtMoTa, txtPhanTramChietKhau, jdc_ngay_bat_dau_voucher, jdc_ngay_ket_thuc_voucher, txt_tien_ap_dung, txt_tien_toi_da, txt_so_luong_vou, rdoDangKM));
//                vou.loadTableVoucher(tblCoupon);
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                txtPhanTramChietKhau.setBackground(Color.WHITE);
                jdc_ngay_bat_dau_voucher.setBackground(Color.WHITE);
                jdc_ngay_bat_dau_voucher.setBackground(Color.WHITE);
                txtMoTa.setBackground(Color.WHITE);
                txt_tien_toi_da.setBackground(Color.WHITE);
                txt_tien_ap_dung.setBackground(Color.WHITE);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        } else {
            if (updateVoucher == 0) {
                return;
            }
            String ma = tblCoupon.getValueAt(i, 0).toString();
            int ma_Voucher = 0;

            try {
                VoucherService vou = new VoucherService();
                if (vou.checkThaoTacVoucher(tblCoupon, txtMaGiamGia, txtMoTa, txtCode, txtPhanTramChietKhau, jdc_ngay_bat_dau_voucher, jdc_ngay_ket_thuc_voucher, txt_tien_ap_dung, txt_tien_toi_da, rdoDangKM, rdoDungKM, txt_so_luong_vou) == 1) {
                    return;
                }
                if (vou.readFromVou(txtMoTa, txtPhanTramChietKhau, jdc_ngay_bat_dau_voucher, jdc_ngay_ket_thuc_voucher, txt_tien_ap_dung, txt_tien_toi_da, txt_so_luong_vou, rdoDangKM) == null) {
                    return;
                }

                int conf = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn cập nhật ?");
                if (conf != 0) {
                    return;
                }
                ma_Voucher = Integer.parseInt(ma);
                vou.capNhatVoucher(vou.readFromVou(txtMoTa, txtPhanTramChietKhau, jdc_ngay_bat_dau_voucher, jdc_ngay_ket_thuc_voucher, txt_tien_ap_dung, txt_tien_toi_da, txt_so_luong_vou, rdoDangKM), ma_Voucher);
//                vou.loadTableVoucher(tblCoupon);
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                txtPhanTramChietKhau.setBackground(Color.WHITE);
                jdc_ngay_bat_dau_voucher.setBackground(Color.WHITE);
                jdc_ngay_ket_thuc_voucher.setBackground(Color.WHITE);
                txtMoTa.setBackground(Color.WHITE);
                txt_tien_toi_da.setBackground(Color.WHITE);
                txt_tien_ap_dung.setBackground(Color.WHITE);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        //-------------------------------------------------------------------------------------------------------------------
    }//GEN-LAST:event_btnLuuMouseClicked

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLuuActionPerformed

    private void txt_tien_toi_daActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_tien_toi_daActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tien_toi_daActionPerformed

    private void txt_so_luong_vouActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_so_luong_vouActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_so_luong_vouActionPerformed

    private void btnLuuThayDoiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLuuThayDoiMouseClicked
        //them và copy----------------------
        //nút lưu điểm quy đổi
        try {
            PointsConversion_Service p = new PointsConversion_Service();
            p.ponts(txtMoney, txtMoney1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //-----------------------
    }//GEN-LAST:event_btnLuuThayDoiMouseClicked

    private void btnResetPointMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetPointMouseClicked
        // thêm và copy----------------------------------------------------
        //---------------nút làm mới điểm quy đổi
        try {
            PointsConversion_Service psv = new PointsConversion_Service();
            txtPoint.setText(String.valueOf(1));
            double tienSangDiem = psv.tienSangDiem();
            double diemSangTien = psv.diemSangTien();
            txtMoney1.setText(String.valueOf(diemSangTien));
            txtMoney.setText(String.valueOf(tienSangDiem));
            System.out.println(diemSangTien + tienSangDiem);
            txtPoint1.setText(String.valueOf(1000));
            txtMoney.setBackground(Color.WHITE);
            txtMoney1.setBackground(Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------
    }//GEN-LAST:event_btnResetPointMouseClicked

    private void tblDiscountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDiscountMouseClicked
        //tạo và copy đây-----------------------------
        updateDis = 1;
        try {
            DiscountService dis = new DiscountService();
            dis.showDiscount(txtMa_chiet_khau, tblDiscount, jdc_ngay_bat_dau, jdc_ngay_ket_thuc, txtMa_chiet_khau, txt_code_chi_tiet, txtMau, txtKieuDang, txtKichThuoc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tbl_san_pham_khuyen_mai.clearSelection();
        txt_tim_sp.setText("");
        txtMau.setEnabled(false);
        txtKichThuoc.setEnabled(false);
        txtKieuDang.setEnabled(false);
        //---------------------------------------------------------
    }//GEN-LAST:event_tblDiscountMouseClicked

    private void btnAddDisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddDisMouseClicked
        //tạo và copy
        //--------------------------------------------------
        //nút thêm giảm giá trực tiếp
        //        tbl_san_pham_khuyen_mai.clearSelection();
        addDis = 1;
        updateDis = 0;
        tblDiscount.clearSelection();
        txtMa_chiet_khau.setText("");
        jdc_ngay_bat_dau.setDate(null);
        jdc_ngay_ket_thuc.setDate(null);
        txt_so_tien_giam.setText("");
        txtMau.setEnabled(false);
        txtKichThuoc.setEnabled(false);
        txtKieuDang.setEnabled(false);
        txtMa_chiet_khau.setBackground(Color.WHITE);
        jdc_ngay_bat_dau.setBackground(Color.WHITE);
        jdc_ngay_ket_thuc.setBackground(Color.WHITE);
        txt_so_tien_giam.setBackground(Color.WHITE);
        try {
            DiscountService dis = new DiscountService();
            dis.loadTableDisscount(tblDiscount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tbl_san_pham_khuyen_mai.clearSelection();
        jdc_ngay_bat_dau.setEnabled(true);
        jdc_ngay_ket_thuc.setEnabled(true);
        txt_so_tien_giam.setEnabled(true);
        txtMa_chiet_khau.setEnabled(true);

        //-----------------------------------------
    }//GEN-LAST:event_btnAddDisMouseClicked

    private void btnEditDisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditDisMouseClicked
        //        // thêm và copy-------------------------------------------------
        //--------------nút cập nhật bảng giảm giá trự tiếp

        updateDis = 1;
        addDis = 0;
        int row = tblDiscount.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Hãy chọn 1 chương trình để cập nhật");
            updateDis = 0;
            return;

        }
        tbl_san_pham_khuyen_mai.clearSelection();
        txtMa_chiet_khau.setEnabled(false);
        jdc_ngay_bat_dau.setEnabled(true);
        jdc_ngay_ket_thuc.setEnabled(true);
        txt_so_tien_giam.setEnabled(true);
        //----------------------------------------------------------------------------
    }//GEN-LAST:event_btnEditDisMouseClicked

    private void btnEditDisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditDisActionPerformed

    }//GEN-LAST:event_btnEditDisActionPerformed

    private void txtMa_chiet_khauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMa_chiet_khauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMa_chiet_khauActionPerformed

    private void btnLamMoi2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLamMoi2MouseClicked
        //tạo và copy đây---------------------------------------------------------
        //nút lưu giảm giá trực tiếp

        int i = tblDiscount.getSelectedRow();

        //nếu bảng không click => thêm
        if (i == -1) {
            if (addDis == 0) {
                return;
            }
            try {
                DiscountService dis = new DiscountService();
                ProductDetails_Service pro = new ProductDetails_Service();
                if (dis.readFromDis(txtMa_chiet_khau, jdc_ngay_bat_dau, jdc_ngay_ket_thuc, txt_so_tien_giam, txt_code_chi_tiet) == null) {
                    return;
                }

                String maChietKhau = txtMa_chiet_khau.getText().trim();
                if (!dis.tim(maChietKhau).isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Mã chiết khấu đã tồn tại không thể thêm");
                    txtMa_chiet_khau.requestFocus();
                    txtMa_chiet_khau.setBackground(Color.YELLOW);
                    return;
                } else {
                    String code = txt_code_chi_tiet.getText().trim();

//                    dis.them(dis.readFromDis(txtMa_chiet_khau, jdc_ngay_bat_dau, jdc_ngay_ket_thuc, txt_so_tien_giam, txt_code_chi_tiet), code);
                    dis.loadTableDisscount(tblDiscount);

                    pro.loadTableProductDetails(tbl_san_pham_khuyen_mai);

                }

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

        } else {
            if (updateDis == 0) {
                return;
            }
            try {
                DiscountService dis = new DiscountService();
                OriginalPriceService op = new OriginalPriceService();

                String maChietKhau = tblDiscount.getValueAt(i, 0).toString();
                if (dis.checkLuu(tblDiscount, jdc_ngay_bat_dau, jdc_ngay_ket_thuc, txt_so_tien_giam) == 1) {
                    return;
                }
                if (dis.readFromDis(txtMa_chiet_khau, jdc_ngay_bat_dau, jdc_ngay_ket_thuc, txt_so_tien_giam, txt_code_chi_tiet) == null) {
                    return;
                }
                //
                DiscountService ds = new DiscountService();
                ProductDetails_Service pro = new ProductDetails_Service();
                OriginalPriceService ops = new OriginalPriceService();
                double index = Double.parseDouble(txt_so_tien_giam.getText());
                String code = txt_code_chi_tiet.getText().trim();
                txtMa_chiet_khau.setEnabled(false);
                jdc_ngay_bat_dau.setEnabled(false);
                jdc_ngay_ket_thuc.setEnabled(false);
                txt_so_tien_giam.setEnabled(false);
                txtMa_chiet_khau.setBackground(Color.WHITE);
                jdc_ngay_bat_dau.setBackground(Color.WHITE);
                jdc_ngay_ket_thuc.setBackground(Color.WHITE);
                jdc_ngay_ket_thuc.setBackground(Color.WHITE);
//                dis.capNhat(dis.readFromDis(txtMa_chiet_khau, jdc_ngay_bat_dau, jdc_ngay_ket_thuc, txt_so_tien_giam, txt_code_chi_tiet), index, code);
                dis.loadTableDisscount(tblDiscount);
                pro.loadTableProductDetails(tbl_san_pham_khuyen_mai);

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        //--------------------------------------------------------------
    }//GEN-LAST:event_btnLamMoi2MouseClicked

    private void tbl_san_pham_khuyen_maiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_san_pham_khuyen_maiMouseClicked
        // TODO add your handling code here:
        //------------------------MouseClicked của bảng tbl_san_pham_khuyen_mai
        tblDiscount.clearSelection();

        DiscountService dis;
        try {
            dis = new DiscountService();
            dis.showBang_chi_tiet_Sp(tbl_san_pham_khuyen_mai, txt_code_chi_tiet, txtMau, txtKieuDang, txtKichThuoc);
            System.out.println("Hết ngáo");
        } catch (SQLException ex) {
            Logger.getLogger(view_TrangChu.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("lỗi");
        }
        txtMau.setEnabled(false);
        txtKichThuoc.setEnabled(false);
        txtKieuDang.setEnabled(false);
        //---------------------------------------------------------------------------------
    }//GEN-LAST:event_tbl_san_pham_khuyen_maiMouseClicked

    private void txt_tim_spCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_tim_spCaretUpdate
        // TODO add your handling code here:
        //------tạo event CaretUpdate cho txt_tim_sp
        try {
            ProductDetails_Service pro = new ProductDetails_Service();
            String index = txt_tim_sp.getText().trim();
            pro.loadTableProductDetailsTim(tbl_san_pham_khuyen_mai, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //--------------------------------------------
    }//GEN-LAST:event_txt_tim_spCaretUpdate

    private void txt_tim_spKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_tim_spKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_txt_tim_spKeyTyped

    private void txt_tim_chuong_trìnhCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_tim_chuong_trìnhCaretUpdate
        // TODO add your handling code here:
        //--tạo CaretUpdate tim_chuong_trình----------------------
        try {
            DiscountService dis = new DiscountService();
            String index = txt_tim_chuong_trình.getText().trim();
            dis.loadTableTim(tblDiscount, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //-----------------------------------------------------
    }//GEN-LAST:event_txt_tim_chuong_trìnhCaretUpdate

    private void TabGiamGiaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabGiamGiaMouseClicked
        // TODO add your handling code here:
        //---------------------------------------tạoTabGiamGiaMouseClicked----------------------
        if (selectedTabIndex == 0) {
            jdc_ngay_bat_dau.setEnabled(false);
            jdc_ngay_ket_thuc.setEnabled(false);
            txtMa_chiet_khau.setEnabled(false);
            txt_so_tien_giam.setEnabled(false);
            jdc_ngay_bat_dau.setDate(null);
            jdc_ngay_ket_thuc.setDate(null);
            txtMa_chiet_khau.setText("");
            txt_so_tien_giam.setText("");
            txt_tim_sp.setText("");
            txt_tim_chuong_trình.setText("");
            txtMau.setText("");
            txtKichThuoc.setText("");
            txtKieuDang.setText("");
            txt_code_chi_tiet.setText("");
            tblDiscount.clearSelection();
            try {
                ProductDetails_Service pro = new ProductDetails_Service();
                DiscountService dis = new DiscountService();
                pro.loadTableProductDetails(tbl_san_pham_khuyen_mai);
                dis.loadTableDisscount(tblDiscount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(selectedTabIndex);
            return;

        } else if (selectedTabIndex == 1) {
            txtPhanTramChietKhau.setText("");
            tblCoupon.clearSelection();

            rdoDangKM.setEnabled(false);
            rdoDungKM.setEnabled(false);
            jdc_ngay_bat_dau_voucher.setDate(null);
            jdc_ngay_ket_thuc_voucher.setDate(null);
            txt_tien_toi_da.setText("");
            txt_tien_ap_dung.setText("");
            txt_so_luong_vou.setText("");
            txtMaGiamGia.setText("");
            txtCode.setText("");
            txtMoTa.setText("");

            jdc_ngay_bat_dau_voucher.setEnabled(false);
            jdc_ngay_ket_thuc_voucher.setEnabled(false);
            txt_tien_toi_da.setEnabled(false);
            txt_tien_ap_dung.setEnabled(false);
            txt_so_luong_vou.setEnabled(false);
            txtMaGiamGia.setEnabled(false);
            txtCode.setEnabled(false);
            txtMoTa.setEnabled(false);
            txt_code_chi_tiet.setEnabled(false);
            try {
                ProductDetails_Service pro = new ProductDetails_Service();
                DiscountService dis = new DiscountService();
                pro.loadTableProductDetails(tbl_san_pham_khuyen_mai);
                dis.loadTableDisscount(tblDiscount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(selectedTabIndex);
            return;
        }

        //-----------------------------------------------------------------------------------
    }//GEN-LAST:event_TabGiamGiaMouseClicked

       
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(View_NhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(View_NhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(View_NhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(View_NhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new View_NhanVien(2).setVisible(true);
                } catch (SQLDataException ex) {
                    Logger.getLogger(View_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(View_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BangHoaDon;
    private javax.swing.JPanel Coupon;
    private javax.swing.JPanel DanhSachKhachHang1;
    private com.toedter.calendar.JDateChooser DenLocTheoKhoangUpdate;
    private javax.swing.JPanel Discount;
    private javax.swing.JPanel MenuHeThong;
    private javax.swing.JPanel SanPhamTrongHoaDon;
    private javax.swing.JTabbedPane TabGiamGia;
    private javax.swing.JPanel ThongKeSP;
    private com.toedter.calendar.JDateChooser TuLocTheoKhoangUpdate;
    private javax.swing.JPanel ViewHeThong;
    private javax.swing.JLabel anhQRcode;
    private javax.swing.JButton btnAddCou;
    private javax.swing.JButton btnAddDis;
    private javax.swing.JButton btnAddProductDetails;
    private javax.swing.JButton btnBackProducts;
    private javax.swing.JButton btnBackkkkkk;
    private javax.swing.JButton btnCapNhatGioHang;
    private javax.swing.JButton btnCapNhatThongTinCN;
    private javax.swing.JButton btnDangXuat;
    private javax.swing.JButton btnDoiMatKhau;
    private javax.swing.JButton btnDowloadQr;
    private javax.swing.JButton btnEditCou;
    private javax.swing.JButton btnEditDis;
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnExcelProductDetail;
    private javax.swing.JButton btnEyeMK;
    private javax.swing.JButton btnEyeMKMoi;
    private javax.swing.JButton btnEyeXacNhan;
    private javax.swing.JButton btnHoanTac1;
    private javax.swing.JButton btnKhachLe;
    private javax.swing.JButton btnLamMoi2;
    private javax.swing.JButton btnLamMoiKH1;
    private javax.swing.JButton btnLamMoiTTSP;
    private javax.swing.JButton btnLamMoiThongKe;
    private javax.swing.JButton btnLocDoanhSoUpdate;
    private javax.swing.JButton btnLocKhoangSanPham;
    private javax.swing.JButton btnLocTheoKhoang;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnLuuThayDoi;
    private javax.swing.JButton btnLuuTruHD;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnNextAll;
    private javax.swing.JButton btnNextt;
    private javax.swing.JButton btnNexttALL;
    private javax.swing.JButton btnPreviou;
    private javax.swing.JButton btnPreviouAll;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnPreviousAll;
    private javax.swing.JButton btnQRcode;
    private javax.swing.JButton btnQuaylai;
    private javax.swing.JButton btnQuetMaQRCodee;
    private javax.swing.JButton btnResetPoint;
    private javax.swing.JButton btnSuaKhachHang1;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThemKHtuHD;
    private javax.swing.JButton btnThemMoiKH1;
    private javax.swing.JButton btnThemMoiThuocTinh;
    private javax.swing.JButton btnThemSPVaoHD;
    private javax.swing.JButton btnTiepTucThanhToan;
    private javax.swing.JButton btnTimKiemSP;
    private javax.swing.JButton btnVoHH;
    private javax.swing.JButton btnXacNhanDMK;
    private javax.swing.JButton btnXemDSSPCT;
    private javax.swing.JButton btnXoaHoaDonCho;
    private javax.swing.JButton btnXoaKhoiGioHang;
    private javax.swing.JButton btnXuatExcelHoaDon;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JPanel camera;
    private javax.swing.JPanel capNhatTichDiem;
    private javax.swing.JComboBox<String> cbbColor;
    private javax.swing.JComboBox<String> cbbDanhMuc;
    private javax.swing.JComboBox<String> cbbGiaBan;
    private javax.swing.JComboBox<String> cbbKichThuocSPCT;
    private javax.swing.JComboBox<String> cbbLoaiSPCT;
    private javax.swing.JComboBox<String> cbbLocNhaCC;
    private javax.swing.JComboBox<String> cbbLocTheoDM;
    private javax.swing.JComboBox<String> cbbLocTrangThaiHoaDon;
    private javax.swing.JComboBox<String> cbbMauSacSPCT;
    private javax.swing.JComboBox<String> cbbNhaCC;
    private javax.swing.JComboBox<String> cbbSizes;
    private javax.swing.JComboBox<String> cbbSoLuong;
    private javax.swing.JComboBox<String> cbbStyle;
    private javax.swing.JComboBox<String> cbbStyles;
    private javax.swing.JComboBox<String> cbbTOP;
    private javax.swing.JComboBox<String> cbbTrangThaiSP;
    private javax.swing.JComboBox<String> cbbVouchers;
    private com.toedter.calendar.JDateChooser dateDen;
    private com.toedter.calendar.JDateChooser dateTu;
    private javax.swing.JPanel formDoiMK;
    private com.toedter.calendar.JYearChooser jDTTNam;
    private com.toedter.calendar.JMonthChooser jDTTThang;
    private com.toedter.calendar.JDateChooser jDateDoanhThuNgay;
    private com.toedter.calendar.JDateChooser jDenNgay;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel164;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel166;
    private javax.swing.JLabel jLabel167;
    private javax.swing.JLabel jLabel168;
    private javax.swing.JLabel jLabel169;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel170;
    private javax.swing.JLabel jLabel171;
    private javax.swing.JLabel jLabel172;
    private javax.swing.JLabel jLabel173;
    private javax.swing.JLabel jLabel174;
    private javax.swing.JLabel jLabel175;
    private javax.swing.JLabel jLabel176;
    private javax.swing.JLabel jLabel177;
    private javax.swing.JLabel jLabel178;
    private javax.swing.JLabel jLabel179;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel180;
    private javax.swing.JLabel jLabel181;
    private javax.swing.JLabel jLabel182;
    private javax.swing.JLabel jLabel183;
    private javax.swing.JLabel jLabel184;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private com.toedter.calendar.JDateChooser jTuNgay;
    private com.toedter.calendar.JDateChooser jdc_ngay_bat_dau;
    private com.toedter.calendar.JDateChooser jdc_ngay_bat_dau_voucher;
    private com.toedter.calendar.JDateChooser jdc_ngay_ket_thuc;
    private com.toedter.calendar.JDateChooser jdc_ngay_ket_thuc_voucher;
    private javax.swing.JPanel jpContainer;
    private javax.swing.JPanel jpQLBH;
    private javax.swing.JPanel jpQLDM;
    private javax.swing.JPanel jpQLHD;
    private javax.swing.JPanel jpQLKH;
    private javax.swing.JPanel jpQLKM;
    private javax.swing.JPanel jpQLSP;
    private javax.swing.JPanel jpQLTK;
    private javax.swing.JPanel jpTK;
    private javax.swing.JPanel jpThongTinCN;
    private javax.swing.JPanel jpViewDoiMK;
    private javax.swing.JPanel jpViewQLBH2;
    private javax.swing.JPanel jpViewQLDM;
    private javax.swing.JPanel jpViewQLHD;
    private javax.swing.JPanel jpViewQLKH;
    private javax.swing.JPanel jpViewQLKM;
    private javax.swing.JPanel jpViewQLSPP;
    private javax.swing.JPanel jpViewQLSPP2;
    private javax.swing.JPanel jpViewQLSPP3;
    private javax.swing.JPanel jpViewQLTK;
    private javax.swing.JPanel jpViewTK;
    private javax.swing.JPanel jp_V_HoaDon;
    private javax.swing.JPanel jp_V_ThemMoiHD;
    private javax.swing.JLabel lblDiemConLai;
    private javax.swing.JLabel lblDoanhThuNgay;
    private javax.swing.JLabel lblDoanhThuThang;
    private javax.swing.JLabel lblDoanhThuTong;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblNhanVienBan;
    private javax.swing.JLabel lblPage;
    private javax.swing.JLabel lblPages;
    private javax.swing.JLabel lblSPDaBan;
    private javax.swing.JLabel lblTienTruocKhiGiam;
    private javax.swing.JLabel lblTongSP;
    private javax.swing.JLabel lblTongTienThanhToan;
    private javax.swing.JLabel lblVAT;
    private javax.swing.JLabel pnWebCam;
    private javax.swing.JRadioButton rdoChuyenKhoan;
    private javax.swing.JRadioButton rdoDangKM;
    private javax.swing.JRadioButton rdoDungKM;
    private javax.swing.JRadioButton rdoRun;
    private javax.swing.JRadioButton rdoStop;
    private javax.swing.JRadioButton rdoTienMat;
    private javax.swing.JPanel spDaBan;
    private javax.swing.JTabbedPane tabQLNV;
    private javax.swing.JTable tblCoupon;
    private javax.swing.JTable tblDSHD;
    private javax.swing.JTable tblDSSPtrongHD;
    private javax.swing.JTable tblDSSanPham;
    private javax.swing.JTable tblDanhSachDM;
    private javax.swing.JTable tblDanhSachSPdaBan;
    private javax.swing.JTable tblDiscount;
    private javax.swing.JTable tblHienThiKetQua;
    private javax.swing.JTable tblHoaDonCho;
    private javax.swing.JTable tblKhachH;
    private javax.swing.JTable tblKichThuoc;
    private javax.swing.JTable tblMauSac;
    private javax.swing.JTable tblNhaCungCap;
    private javax.swing.JTable tblPhongCach;
    private javax.swing.JTable tblSPCT;
    private javax.swing.JTable tblSanPhamTrongHoaDon;
    private javax.swing.JTable tbl_san_pham_khuyen_mai;
    private javax.swing.JTabbedPane tbpThuocTinh;
    private javax.swing.JPanel tongSP;
    private javax.swing.JTextField txtCode;
    private javax.swing.JPasswordField txtConfirmPassword;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtDiaChiNCC;
    private javax.swing.JTextField txtDiemDaDung;
    private javax.swing.JTextField txtDiemTichLuy;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEmailNCC;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtITenKhachHang1;
    private javax.swing.JTextField txtKichThuoc;
    private javax.swing.JTextField txtKichThuoccc;
    private javax.swing.JTextField txtKieuDang;
    private javax.swing.JTextField txtLoaiSPpp;
    private javax.swing.JTextField txtLocHoaDonTheoMaHD;
    private javax.swing.JTextField txtMaCTSP;
    private javax.swing.JTextField txtMaDM;
    private javax.swing.JTextField txtMaGiamGia;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtMaKT;
    private javax.swing.JTextField txtMaMau;
    private javax.swing.JTextField txtMaPC;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextField txtMaSPCT;
    private javax.swing.JTextField txtMaSPCT1;
    private javax.swing.JTextField txtMaSanPham;
    private javax.swing.JTextField txtMa_chiet_khau;
    private javax.swing.JTextField txtMau;
    private javax.swing.JTextField txtMauSaccc;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextArea txtMoTaPC;
    private javax.swing.JTextArea txtMoTaSize;
    private javax.swing.JTextField txtMoney;
    private javax.swing.JTextField txtMoney1;
    private javax.swing.JTextArea txtMotaSPP;
    private javax.swing.JTextField txtNCC;
    private javax.swing.JPasswordField txtNewPassword;
    private javax.swing.JTextField txtNhapSoLuong;
    private javax.swing.JTextField txtNhapSoLuong1;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPhanTramChietKhau;
    private javax.swing.JTextField txtPhongCachhh;
    private javax.swing.JTextField txtPoint;
    private javax.swing.JTextField txtPoint1;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtSDTKhachHang;
    private javax.swing.JTextField txtSDTNCC;
    private javax.swing.JTextField txtSdt;
    private javax.swing.JTextField txtSearhProductDetail;
    private javax.swing.JTextField txtSoDiemDaDung;
    private javax.swing.JTextField txtSoDiienThoai;
    private javax.swing.JTextField txtTenDM;
    private javax.swing.JTextField txtTenKhachHang;
    private javax.swing.JTextField txtTenKichThuoc;
    private javax.swing.JTextField txtTenMau;
    private javax.swing.JTextField txtTenNCC;
    private javax.swing.JTextField txtTenPhongCach;
    private javax.swing.JTextField txtTenSP;
    private javax.swing.JTextField txtTenSanPham;
    private javax.swing.JTextField txtTimKiemSP;
    private javax.swing.JTextField txtTimKiemTheoMa;
    private javax.swing.JTextField txtTinhDiem;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JTextField txtUsername1;
    private javax.swing.JTextField txtVaiTro;
    private javax.swing.JTextField txt_code_chi_tiet;
    private javax.swing.JTextField txt_so_luong_vou;
    private javax.swing.JTextField txt_so_tien_giam;
    private javax.swing.JTextField txt_tien_ap_dung;
    private javax.swing.JTextField txt_tien_toi_da;
    private javax.swing.JTextField txt_tim_chuong_trình;
    private javax.swing.JTextField txt_tim_sp;
    // End of variables declaration//GEN-END:variables
    

}
