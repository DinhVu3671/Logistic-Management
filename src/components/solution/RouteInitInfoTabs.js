import { CButton, CCard, CCardBody, CCardHeader, CCol, CCollapse, CContainer, CDataTable, CFormGroup, CInput, CLabel, CNav, CNavItem, CNavLink, CRow, CSwitch, CTabContent, CTabPane, CTabs } from "@coreui/react";
import React, { Component } from "react";
import { secondsToHHMMSS } from "../../utilities/utility";


class RouteInitInfoTabs extends Component {

    constructor(props) {
        super(props);
        this.state = {
            depots: this.processDepotsData(this.props.depots),
            customers: this.processCustomersData(this.props.customers),
            orders: this.props.orders,
            vehicles: this.props.vehicles,
            problemAssumption: this.props.problemAssumption,
        };
    }

    processCustomersData = (customersData) => {
        customersData.map((customer, index) => {
            customer.startTimeStr = secondsToHHMMSS(customer.startTime);
            customer.endTimeStr = secondsToHHMMSS(customer.endTime);
        });
        return customersData;
    }

    processDepotsData = (depotsData) => {
        depotsData.map((depot, index) => {
            depot.startTimeStr = secondsToHHMMSS(depot.startTime);
            depot.endTimeStr = secondsToHHMMSS(depot.endTime);
        });
        return depotsData;
    }

    render() {

        return (
            <CContainer sm="12">
                <CRow>
                    <CCol >
                        <CCard>
                            <CCardHeader>
                                Input Info
                        </CCardHeader>
                            <CCardBody>
                                <CTabs>
                                    <CNav variant="tabs">
                                        <CNavItem>
                                            <CNavLink>
                                                Orders
                                            </CNavLink>
                                        </CNavItem>
                                        <CNavItem>
                                            <CNavLink>
                                                Customers
                                        </CNavLink>
                                        </CNavItem>
                                        <CNavItem>
                                            <CNavLink>
                                                Depots
                                        </CNavLink>
                                        </CNavItem>
                                        <CNavItem>
                                            <CNavLink>
                                                Vehicles
                                            </CNavLink>
                                        </CNavItem>
                                        <CNavItem>
                                            <CNavLink>
                                                Configuration
                                            </CNavLink>
                                        </CNavItem>
                                    </CNav>
                                    <CTabContent>
                                        <CTabPane>
                                            <CDataTable
                                                items={this.state.orders}
                                                fields={[
                                                    { key: 'code', label: 'Mã đơn hàng' },
                                                    // { key: 'depotCode', label: 'Mã kho hàng' },
                                                    { key: 'customerCode', label: 'Mã khách hàng' },
                                                    { key: 'orderValue', label: 'Giá trị đơn hàng(VND)' },
                                                    { key: 'weight', label: 'Khối lượng(kg)' },
                                                    { key: 'capacity', label: 'Thể tích(m3)' },
                                                    { key: 'productTypeNumber', label: 'Số loại sản phẩm' },

                                                ]}
                                                hover
                                                striped
                                                bordered
                                                size="lg"
                                                itemsPerPage={5}
                                                pagination
                                                scopedSlots={
                                                    {
                                                        'depotCode':
                                                            (item) => {
                                                                return (
                                                                    <td className="py-2">
                                                                        <span>{item.depot.code}</span>
                                                                    </td>
                                                                )
                                                            },
                                                        'customerCode':
                                                            (item) => {
                                                                return (
                                                                    <td className="py-2">
                                                                        <span>{item.customer.code}</span>
                                                                    </td>
                                                                )
                                                            },

                                                    }}
                                            />
                                        </CTabPane>
                                        <CTabPane>
                                            <CDataTable
                                                items={this.state.customers}
                                                fields={[
                                                    { key: 'name', _classes: 'font-weight-bold', label: 'Tên khách hàng' },
                                                    { key: 'code', label: 'Mã khách hàng' },
                                                    { key: 'longitude', label: 'Kinh độ' },
                                                    { key: 'latitude', label: 'Vĩ độ' },
                                                    { key: 'address', label: 'Địa chỉ' },
                                                    { key: 'startTimeStr', label: 'Thời điểm mở cửa' },
                                                    { key: 'endTimeStr', label: 'Thời điểm đóng cửa' },

                                                ]}
                                                hover
                                                striped
                                                bordered
                                                size="lg"
                                                itemsPerPage={5}
                                                pagination
                                                scopedSlots={{
                                                }}
                                            />
                                        </CTabPane>
                                        <CTabPane>
                                            <CDataTable
                                                items={this.state.depots}
                                                fields={[
                                                    { key: 'name', _classes: 'font-weight-bold', label: 'Tên kho' },
                                                    { key: 'code', label: 'Mã kho' },
                                                    { key: 'longitude', label: 'Kinh độ' },
                                                    { key: 'latitude', label: 'Vĩ độ' },
                                                    { key: 'address', label: 'Địa chỉ' },
                                                    { key: 'startTimeStr', label: 'Thời điểm mở cửa' },
                                                    { key: 'endTimeStr', label: 'Thời điểm đóng cửa' },
                                                    // { key: 'unloadingCost', label: 'Phí dỡ hàng(VND)' },
                                                ]}
                                                hover
                                                striped
                                                bordered
                                                size="lg"
                                                itemsPerPage={5}
                                                pagination
                                                scopedSlots={{
                                                }}
                                            />
                                        </CTabPane>
                                        <CTabPane>
                                            <CDataTable
                                                items={this.state.vehicles}
                                                fields={[
                                                    { key: 'name', _classes: 'font-weight-bold', label: 'Tên Xe' },
                                                    { key: 'capacity', label: 'Sức chứa(kg/m3)' },
                                                    { key: 'averageFeeTransport', label: 'Phí di chuyển(VND/km)' },
                                                    { key: 'averageVelocity', label: 'Vận tốc trung bình(km/h)' },
                                                    { key: 'driverName', label: 'Tên lái xe' },
                                                    { key: 'type', label: 'Loại xe' },

                                                ]}
                                                hover
                                                striped
                                                bordered
                                                size="lg"
                                                itemsPerPage={5}
                                                pagination
                                                scopedSlots={
                                                    {
                                                        'capacity':
                                                            (item) => {
                                                                return (
                                                                    <td className="py-2">
                                                                        <span>{item.maxLoadWeight + "/" + item.maxCapacity}</span>
                                                                    </td>
                                                                )
                                                            },
                                                    }}
                                            />
                                        </CTabPane>
                                        <CTabPane>
                                            <CRow row>
                                                <CCol xs="6">
                                                    <CCard xs="6" className="mb-0">
                                                        <CCardHeader id="headingOne">
                                                            <h5 className="m-0 p-0">Cơ bản</h5>
                                                        </CCardHeader>
                                                        <CCardBody>
                                                            <CFormGroup row xs="10" className="my-0">
                                                                <CCol xs="5">
                                                                    <CFormGroup>
                                                                        <CLabel htmlFor="maxTravelTime">Thời gian chạy xe tối đa(giờ)</CLabel>
                                                                        <CInput id="maxTravelTime" name="maxTravelTime" value={this.props.problemAssumption.maxTravelTime} disabled={true} placeholder="max travel time" type='number' step="0.01" />
                                                                    </CFormGroup>
                                                                </CCol>
                                                                <CCol xs="5">
                                                                    <CFormGroup>
                                                                        <CLabel htmlFor="maxDistance">Quãng đường chạy tối đa(km)</CLabel>
                                                                        <CInput id="maxDistance" name="maxDistance" value={this.props.problemAssumption.maxDistance} disabled={true} placeholder="max distance" type='number' step="0.1" />
                                                                    </CFormGroup>
                                                                </CCol>
                                                            </CFormGroup>
                                                            <CFormGroup row xs="10" className="my-0">
                                                                <CCol xs="3">
                                                                    <CFormGroup>
                                                                        <CLabel htmlFor="maxTime">Thời gian chạy thuật toán tối đa</CLabel>
                                                                        <CInput id="maxTime" name="maxTime" value={this.props.problemAssumption.maxTime} disabled={true} placeholder="maxTime" type='number' step="0.1" />
                                                                    </CFormGroup>
                                                                </CCol>
                                                                <CCol xs="3">
                                                                    <CFormGroup>
                                                                        <CRow>
                                                                            <CLabel htmlFor="isAllowedViolateTW.">Cho phép vi phạm khung thời gian</CLabel>
                                                                        </CRow>
                                                                        <CSwitch id='isAllowedViolateTW' name="isAllowedViolateTW" size="lg" checked={this.props.problemAssumption.isAllowedViolateTW} color={'success'} className={'mx-1'} variant={'3d'} labelOn={'\u2713'} labelOff={'\u2715'} />
                                                                    </CFormGroup>
                                                                </CCol>
                                                                <CCol xs="2">
                                                                    <CFormGroup>
                                                                        <CRow>
                                                                            <CLabel htmlFor="isExcludeProduct">Tự động loại trừ hàng hóa</CLabel>
                                                                        </CRow>
                                                                        <CSwitch id='isExcludeProduct' name="isExcludeProduct" size="lg" checked={this.props.problemAssumption.isExcludeProduct} color={'success'} className={'mx-1'} variant={'3d'} labelOn={'\u2713'} labelOff={'\u2715'} />
                                                                    </CFormGroup>
                                                                </CCol>
                                                            </CFormGroup>
                                                        </CCardBody>
                                                    </CCard>
                                                </CCol>
                                                <CCol xs="6">
                                                    <CCard className="mb-0">
                                                        <CCardHeader id="headingTwo">
                                                            <h5 className="m-0 p-0">Nâng cao</h5>
                                                        </CCardHeader>
                                                        <CCardBody>
                                                            <CFormGroup row className="my-0">
                                                                <CCol xs="4">
                                                                    <CFormGroup>
                                                                        <CLabel htmlFor="popSize">Kích thước quần thể (cá thể)</CLabel>
                                                                        <CInput id="popSize" name="popSize" value={this.props.problemAssumption.popSize} disabled={true} placeholder="popSize" type='number' step="1" />
                                                                    </CFormGroup>
                                                                </CCol>
                                                                <CCol xs="4">
                                                                    <CFormGroup>
                                                                        <CLabel htmlFor="eliteSize">Kích thước tập tinh hoa (cá thể)</CLabel>
                                                                        <CInput id="eliteSize" name="eliteSize" value={this.props.problemAssumption.eliteSize} disabled={true} placeholder="eliteSize" type='number' step="1" />
                                                                    </CFormGroup>
                                                                </CCol>
                                                            </CFormGroup>
                                                            <CFormGroup row className="my-0">
                                                                <CCol xs="4">
                                                                    <CFormGroup>
                                                                        <CLabel htmlFor="maxGen">Số thế hệ tối đa được sinh</CLabel>
                                                                        <CInput id="maxGen" name="maxGen" value={this.props.problemAssumption.maxGen} disabled={true} placeholder="maxGen" type='number' step="1" />
                                                                    </CFormGroup>
                                                                </CCol>
                                                                <CCol xs="4">
                                                                    <CFormGroup>
                                                                        <CLabel htmlFor="maxGenImprove">Số thế hệ tối đa không cải thiện</CLabel>
                                                                        <CInput id="maxGenImprove" name="maxGenImprove" value={this.props.problemAssumption.maxGenImprove} disabled={true} placeholder="maxGen" type='number' step="1" />
                                                                    </CFormGroup>
                                                                </CCol>
                                                            </CFormGroup>
                                                            <CFormGroup row className="my-0">
                                                                <CCol xs="4">
                                                                    <CFormGroup>
                                                                        <CLabel htmlFor="probCrossover">Xác suất Lai ghép</CLabel>
                                                                        <CInput id="probCrossover" name="probCrossover" value={this.props.problemAssumption.probCrossover} disabled={true} placeholder="probCrossover" type='number' step="0.01" />
                                                                    </CFormGroup>
                                                                </CCol>
                                                                <CCol xs="4">
                                                                    <CFormGroup>
                                                                        <CLabel htmlFor="probMutation">Xác suất Đột biến</CLabel>
                                                                        <CInput id="probMutation" name="probMutation" value={this.props.problemAssumption.probMutation} disabled={true} placeholder="probMutation" type='number' step="0.01" />
                                                                    </CFormGroup>
                                                                </CCol>
                                                            </CFormGroup>
                                                            <CFormGroup row className="my-0">
                                                                <CCol xs="4">
                                                                    <CFormGroup>
                                                                        <CLabel htmlFor="tournamentSize">Số cá thể được chọn</CLabel>
                                                                        <CInput id="tournamentSize" name="tournamentSize" value={this.props.problemAssumption.tournamentSize} disabled={true} placeholder="tournamentSize" type='number' step="1" />
                                                                    </CFormGroup>
                                                                </CCol>
                                                                <CCol xs="4">
                                                                    <CFormGroup>
                                                                        <CLabel htmlFor="selectionRate">Tỷ lệ tập chọn lọc</CLabel>
                                                                        <CInput id="selectionRate" name="selectionRate" value={this.props.problemAssumption.probMutation} disabled={true} placeholder="selectionRate" type='number' step="1" />
                                                                    </CFormGroup>
                                                                </CCol>
                                                            </CFormGroup>
                                                        </CCardBody>
                                                    </CCard>
                                                </CCol>
                                            </CRow>
                                        </CTabPane>
                                    </CTabContent>
                                </CTabs>
                            </CCardBody>
                        </CCard>
                    </CCol>
                </CRow>
            </CContainer>
        );
    }

}

export default RouteInitInfoTabs;
