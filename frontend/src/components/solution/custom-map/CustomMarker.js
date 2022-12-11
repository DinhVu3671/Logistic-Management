import React, { Component } from "react";
import { getIconURL, getOrdinalLoadingOrders } from "../../../utilities/utility";
const { Marker, InfoWindow } = require("react-google-maps");

class CustomMarker extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: this.props.marker.id,
            code: this.props.marker.code,
            lat: this.props.marker.lat,
            lng: this.props.marker.lng,
            infoWindow: this.props.marker.infoWindow,
            color: this.props.marker.color,
            icon: getIconURL(this.props.type),
        }
    }

    hideMarkerInfo = () => {
        let marker = this.props.marker;
        marker.isShowInfo = false;
        this.props.setShow(marker);
    }

    render() {
        console.log("infoWindow: " + this.state.infoWindow)
        console.log(this.props.marker)
        return (
            <Marker
                key={this.state.id}
                icon={this.state.icon}
                label={{
                    color: this.state.color,
                    text: this.state.code,
                }}
                position={{
                    lat: parseFloat(this.state.lat),
                    lng: parseFloat(this.state.lng),
                }}
            >
                {this.props.marker.isShowInfo === true && this.props.marker.infoWindow &&
                    <InfoWindow onCloseClick={() => this.hideMarkerInfo()}>
                        {this.props.marker.infoWindow &&
                            <div>
                                <div>
                                    <p><b>{"Thời điểm đến: "}</b>{this.props.marker.infoWindow.startTime}</p>
                                </div>
                                <div>
                                    <p><b>{"Thời điểm dời: "}</b>{this.props.marker.infoWindow.endTime}</p>
                                </div>
                                {this.props.marker.infoWindow.customerCode &&
                                    <div>
                                        <p><b>{"Mã khách hàng: "}</b>{this.props.marker.infoWindow.customerCode}</p>
                                    </div>
                                }
                                {this.props.marker.infoWindow.depotCode &&
                                    <div>
                                        <p><b>{"Mã kho hàng: "}</b>{this.props.marker.infoWindow.depotCode}</p>
                                    </div>
                                }
                                {this.props.marker.infoWindow.bill &&
                                    <div>
                                        <p><b>{"Mã đơn hàng: "}</b>{this.props.marker.infoWindow.bill.order.code}</p>
                                        <p><b>{"Khối lượng hàng: "}</b>{this.props.marker.infoWindow.bill.order.weight + " kg"}</p>
                                        <p><b>{"Thể tích hàng: "}</b>{this.props.marker.infoWindow.bill.order.capacity + " m3"}</p>
                                        <p><b>{"Số loại hàng: "}</b>{this.props.marker.infoWindow.bill.order.productTypeNumber}</p>
                                        <p><b>{"Tổng số tiền thu: "}</b>{this.props.marker.infoWindow.bill.totalAmount + " VND"}</p>
                                        <p><b>{"Giá trị đơn hàng: "}</b>{this.props.marker.infoWindow.bill.orderValue + " VND"}</p>
                                        <p><b>{"Phí dỡ hàng: "}</b>{this.props.marker.infoWindow.bill.unloadingFee + " VND"}</p>
                                    </div>
                                }
                                {this.props.marker.infoWindow.ordinalLoadingOrders &&
                                    <div>
                                        <p><b>{"Thứ tự xếp đơn hàng: "}</b>{getOrdinalLoadingOrders(this.props.marker.infoWindow.ordinalLoadingOrders)}</p>
                                    </div>
                                }
                                {this.props.marker.infoWindow.fillRateCapacity &&
                                    <div>
                                        <p><b>{"Tỷ lệ lấp đầy thể tích: "}</b>{this.props.marker.infoWindow.fillRateCapacity + " %"}</p>
                                    </div>
                                }
                                {this.props.marker.infoWindow.fillRateLoadWeight &&
                                    <div>
                                        <p><b>{"Tỷ lệ lấp đầy khối lượng: "}</b>{this.props.marker.infoWindow.fillRateLoadWeight + " %"}</p>
                                    </div>
                                }
                                {this.props.marker.infoWindow.travelTime &&
                                    <div>
                                        <p><b>{"Thời gian di chuyển: "}</b>{(Math.round((this.props.marker.infoWindow.travelTime / 60) * 10) / 10) + " phút"}</p>
                                    </div>
                                }
                            </div>

                        }

                    </InfoWindow>
                }
            </Marker>);
    }
}

export default CustomMarker

