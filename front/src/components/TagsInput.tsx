import { Autocomplete, CircularProgress, TextField } from "@mui/material";

type TagsInputProps = {
  tags: string[];
  isLoading: boolean;
  tagsValue: string[];
  setTagsValue: (tagsValue: string[]) => void;
};

const TagsInput = ({
  tags,
  isLoading,
  tagsValue,
  setTagsValue,
}: TagsInputProps) => {
  return (
    <>
      <Autocomplete
        multiple
        id="tags"
        options={tags}
        value={tagsValue}
        onChange={(_, value) => setTagsValue(value)}
        freeSolo
        loading={isLoading}
        getOptionLabel={(option) => option}
        renderInput={(params) => (
          <TextField
            {...params}
            variant="standard"
            label="Tags"
            placeholder="Add tags"
            fullWidth
            margin="normal"
            slotProps={{
              input: {
                ...params.InputProps,
                endAdornment: (
                  <>
                    {isLoading ? (
                      <CircularProgress color="inherit" size={20} />
                    ) : null}
                    {params.InputProps.endAdornment}
                  </>
                ),
              },
            }}
          />
        )}
      />
    </>
  );
};

export default TagsInput;