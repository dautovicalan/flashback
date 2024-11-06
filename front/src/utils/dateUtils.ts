import dayjs, { Dayjs } from "dayjs";

export const toFormattedDate = (date: string) => {
  return new Date(date).toLocaleDateString("en-US", {
    year: "numeric",
    month: "long",
    day: "numeric",
  });
};

export const areValidDates = (fromDate: Dayjs | null, toDate: Dayjs | null) => {
  if (fromDate === null || toDate === null) {
    return true;
  }
  if (fromDate.isAfter(toDate)) {
    return false;
  }
  if (dayjs(fromDate).isValid() && dayjs(toDate).isValid()) {
    return true;
  }
  return false;
};
